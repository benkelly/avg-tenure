package com.example.configuration;


import com.example.service.EmployeeService;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.InputResult;
import de.codeshelf.consoleui.prompt.ListResult;
import de.codeshelf.consoleui.prompt.PromtResultItemIF;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import jline.TerminalFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Scanner;


public class MenuBuilder {

  private static EmployeeService employeeService = new EmployeeService();

  public static void buildMenu() throws IOException {
    ConsolePrompt prompt = new ConsolePrompt();

    try {

      PromptBuilder promptBuilder = prompt.getPromptBuilder();
      boolean running = true;

      while (running) {
        // 1. Define the Menu
        promptBuilder.createListPrompt()
                .name("mainMenu")
                .message("Employee Management System (Use arrow keys or j/k to navigate)")
                .newItem("list").text("List Employees").add()
                .newItem("getTenureDays").text("List by Tenure Days").add()
                .newItem("getTenureYears").text("List by Tenure Years").add()
                .newItem("tenureFilter").text("List by Tenure Filter").add()
                .newItem("calc").text("Calculate Average Age").add()
                .newItem("exit").text("Exit").add()
                .addPrompt();

        // 2. Render and wait for input
        HashMap<String, ? extends PromtResultItemIF> result = prompt.prompt(promptBuilder.build());

        // 3. Handle the choice
        ListResult selected = (ListResult) result.get("mainMenu");

        switch (selected.getSelectedId()) {
          case "list":
            System.out.println("\n--- Listing all employees... ---");
            employeeService.displayEmployeeTable();
            waitForUser(prompt); // Pause so user can read
            break;
          case "getTenureDays":
            System.out.println("\n--- Listing by Tenure Days... ---\n");
            employeeService.displayEmployeeTenureTable();
            waitForUser(prompt);
            break;
          case "getTenureYears":
            System.out.println("\n--- Listing by Tenure Years... ---\n");
            employeeService.displayEmployeeTenureYearsTable();
            waitForUser(prompt);
            break;
          case "tenureFilter":
            ConsolePrompt datePrompt = new ConsolePrompt();
            PromptBuilder dateBuilder = datePrompt.getPromptBuilder();

            dateBuilder.createInputPrompt()
                    .name("startDate")
                    .message("Enter Start Date (YYYY-MM-DD):")
                    .addPrompt();

            dateBuilder.createInputPrompt()
                    .name("endDate")
                    .message("Enter End Date (YYYY-MM-DD):")
                    .addPrompt();

            HashMap<String, ? extends PromtResultItemIF> dateResults = datePrompt.prompt(dateBuilder.build());

            String startStr = ((InputResult) dateResults.get("startDate")).getInput();
            String endStr = ((InputResult) dateResults.get("endDate")).getInput();

            LocalDate start = LocalDate.parse(startStr);
            LocalDate end = LocalDate.parse(endStr);

            System.out.println("Processing range: " + start + " to " + end);
            employeeService.displayEmployeesByTenureFilter(start, end);
            waitForUser(prompt);
            break;
          case "calc":
            System.out.println("\n--- Calculating average age... ---");
            System.out.println("Average Age: " + employeeService.calculateAverageAge());
            waitForUser(prompt); // Pause so user can read
            break;
          case "exit":
            System.out.println("\n--- Exiting the program. Goodbye! ---");
            running = false;
            break;
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      try {
        TerminalFactory.get().restore();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private static void waitForUser(ConsolePrompt prompt) throws Exception {
    System.out.println("\nPress Enter to return to the main menu...");
    new Scanner(System.in).nextLine();
  }
}
