# avg-tenure
---

## Java Assignment: Employee Tenure Analyser
 
## Overview
 
You will build a console-based Java application that reads employee data from a JSON file and computes average tenure statistics for a filtered cohort of employees. The assignment covers file I/O, JSON parsing, date handling, object-oriented design, and basic statistical computation.
 
This is the first part of a two-part series. The second assignment will extend this application into a REST API, so clean class boundaries and a well-defined domain model matter here.
 
---
 
## Learning Objectives
 
By completing this assignment you will demonstrate:
 
- Designing and implementing a clean domain model
- Reading and parsing JSON data in Java using the standard library or a single permitted dependency
- Working with `java.time` for date arithmetic
- Applying the single-responsibility principle across multiple classes
- Writing defensive, robust code with meaningful error handling
- Computing statistics over a filtered dataset
---
 
## The JSON File
 
You are provided with a sample data file: `employees.json`. You may extend it or create your own, but it must follow this schema exactly:
 
```json
[
  {
    "id": 1,
    "first_name": "Alice",
    "last_name": "Murphy",
    "date_of_birth": "1985-03-14",
    "hire_date": "2010-06-01",
    "end_date": "2023-11-30"
  },
  {
    "id": 2,
    "first_name": "Brian",
    "last_name": "Ó Súilleabháin",
    "date_of_birth": "1990-07-22",
    "hire_date": "2015-01-15",
    "end_date": null
  },
  {
    "id": 3,
    "first_name": "Clara",
    "last_name": "Ní Bhriain",
    "date_of_birth": "1978-11-05",
    "hire_date": "2008-04-10",
    "end_date": "2021-07-01"
  }
]
```
 
**Field rules:**
 
- `date_of_birth`, `hire_date`, and `end_date` are ISO 8601 format: `YYYY-MM-DD`
- `end_date` may be `null`, indicating the employee is currently active. Treat today's date as their end date for tenure calculation.
- `id` is a positive integer, unique per object.
- Objects with malformed dates or missing required fields must be skipped, with a warning printed to stderr.
The sample file must contain a minimum of 20 employees with varied hire and end dates spanning at least 10 years.
 
---
 
## Application Requirements
 
### 1. Data Layer
 
Implement an `Employee` class with the following:
 
- Fields mapping directly to the CSV columns, using appropriate types (`LocalDate`, `long`, `String`)
- A computed method `getTenureDays()` that returns the number of days between `hire_date` and `end_date` (or today if `end_date` is absent)
- A computed method `getTenureYears()` returning tenure as a decimal value (days ÷ 365.25)
- No business logic beyond these two computations
Implement a `JsonEmployeeReader` class responsible solely for:
 
- Opening and reading `employees.json` from a given file path
- Parsing the JSON array into a `List<Employee>`
- Logging malformed objects to stderr and continuing
- Returning a `List<Employee>`
You may use **Jackson Databind** (`com.fasterxml.jackson.core:jackson-databind`) as your JSON library. No other third-party dependencies are permitted. If you are comfortable doing so without a library, the standard `javax.json` API (included in Java EE / Jakarta EE, or available via the `jakarta.json` reference implementation) is also acceptable.
 
### 2. Filtering
 
Implement a `TenureFilter` class that accepts a `List<Employee>` and filters it to employees whose `hire_date` falls within a caller-supplied date range (inclusive on both ends).
 
The filter must:
 
- Accept `LocalDate` start and end values
- Return only employees whose `hire_date` is on or after the start date and on or before the end date
- Return an empty list (not throw) if no employees match
### 3. Statistics
 
Implement a `TenureStatistics` class that accepts a `List<Employee>` and computes:
 
- Average tenure in years across the list
- The employee with the shortest tenure
- The employee with the longest tenure
- Total headcount in the cohort
Each value must be available via a dedicated getter. The class must throw `IllegalArgumentException` with a descriptive message if called with an empty list.
 
### 4. User Interface
 
The main class must present a simple console menu on startup:
 
```
Employee Tenure Analyser
========================
1. Analyse tenure for a date range
2. Quit
 
Enter choice:
```
 
When option 1 is chosen:
 
- Prompt the user for a start date and an end date in `YYYY-MM-DD` format
- Validate the input and re-prompt on invalid dates
- Validate that start date is not after end date
- Print a summary in the following format:
```
Results for hire dates 2010-01-01 to 2018-12-31
-------------------------------------------------
Employees in cohort : 14
Average tenure      : 7.43 years
Shortest tenure     : Brian Ó Súilleabháin (2.1 years)
Longest tenure      : Clara Ní Bhriain (13.8 years)
```
 
Return to the menu after displaying results.
 
---
 
## Design Constraints
 
- You must use `java.time.LocalDate` and `java.time.temporal.ChronoUnit` for all date arithmetic. Do not use `java.util.Date` or `java.util.Calendar`.
- JSON parsing must be handled by `JsonEmployeeReader` only. No other class may read the file directly.
- You may use Jackson Databind for JSON parsing. No other third-party libraries are permitted.
- Classes must follow single-responsibility: no class should handle both I/O and business logic.
- `main()` must contain no business logic. It may only wire dependencies together and call the menu loop.
- All numeric output must be rounded to 2 decimal places using `String.format`.
- The application must not crash on a malformed JSON object. Errors must be caught and reported, and processing must continue.
---
 
## What You Must Submit
 
```
/src
  Employee.java
  JsonEmployeeReader.java
  TenureFilter.java
  TenureStatistics.java
  Main.java
employees.json
README.md
```
 
`README.md` must include:
 
- How to compile and run the application
- A brief description of each class and its responsibility
- One design decision you made and why
---
 
## Assessment Criteria
 
| Area | Weight | What is looked at |
|---|---|---|
| Correctness | 35% | Filtering and average calculation produce correct results across edge cases |
| Design | 25% | SRP adherence, appropriate encapsulation, sensible class boundaries |
| Robustness | 20% | Malformed JSON objects handled gracefully, invalid user input rejected cleanly |
| Code quality | 15% | Naming, readability, no dead code, no magic strings or numbers |
| README | 5% | Clear, accurate, thoughtful |
 
---
 
## Edge Cases You Must Handle
 
- An employee with a `null` `end_date` (still active)
- An employee whose `hire_date` falls exactly on the filter boundary dates
- A JSON object missing a required field
- A JSON object with a date string that cannot be parsed
- A filter range that matches zero employees (print a clear message, do not crash)
- A JSON file that contains an empty array
---
 
## Extension (Optional, Ungraded)
 
If you finish early, consider:
 
- Grouping results by year of hire and printing a per-year average tenure breakdown
- Sorting the cohort output by tenure descending before printing
- Adding a second menu option that prints the top 5 longest-serving employees across all hire dates
---
 
## Notes
 
- You may use Java 11 or later.
- Do not use streams unless you are comfortable with them; a `for` loop is perfectly acceptable.
- The marker will run your code against a different `employees.json` file. Design with that in mind.
- Structure your `Employee` class with clean getters and no parsing logic inside it. The next assignment will expose this model directly through a REST API, so its shape matters.
