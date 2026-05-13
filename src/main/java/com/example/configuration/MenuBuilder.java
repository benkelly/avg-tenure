package com.example.configuration;


import com.example.service.EmployeeService;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.ListResult;
import de.codeshelf.consoleui.prompt.PromtResultItemIF;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import jline.TerminalFactory;

import java.io.IOException;
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
        // 2. Define the Menu
        promptBuilder.createListPrompt()
                .name("mainMenu")                      // Key used to retrieve the result
                .message("Employee Management System (Use arrow keys or j/k to navigate)") // Title shown to user
                .newItem("list").text("List Employees").add()
                .newItem("getTenureDays").text("List by Tenure Days").add()
                .newItem("getTenureYears").text("List by Tenure Years").add()
                .newItem("tenureFilter").text("List by Tenure Filter").add()
                .newItem("calc").text("Calculate Average Age").add()
                .newItem("exit").text("Exit").add()
                .addPrompt();

        // 3. Render and wait for input
        HashMap<String, ? extends PromtResultItemIF> result = prompt.prompt(promptBuilder.build());

        // 4. Handle the choice
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
            break;
          case "getTenureYears":
            System.out.println("\n--- Listing by Tenure Years... ---\n");
            break;
          case "tenureFilter":
            IO.println("Enter start date in the format YYYY-MM_DD");
            String startDate = IO.readln();
            IO.println("Enter end date in the format YYYY-MM-DD");
            String endDate = IO.readln();
//            System.out.println();
//            ConsolePrompt datePrompt = new ConsolePrompt();
//            PromptBuilder dateBuilder = datePrompt.getPromptBuilder();
//
//            dateBuilder.createInputPrompt()
//                    .name("startDate")
//                    .message("Enter Start Date (YYYY-MM-DD):")
//                    .addValidator(new DateValidator()) // Custom validator below
//                    .addPrompt();
//
//            dateBuilder.createInputPrompt()
//                    .name("endDate")
//                    .message("Enter End Date (YYYY-MM-DD):")
//                    .addValidator(new DateValidator())
//                    .addPrompt();
//
//            // This returns BOTH answers in one map
//            HashMap<String, ? extends PromtResultItemIF> dateResults = datePrompt.prompt(dateBuilder.build());
//
//            // Extract the strings
//            String startStr = ((InputResult) dateResults.get("startDate")).getInput();
//            String endStr = ((InputResult) dateResults.get("endDate")).getInput();
//
//            // Convert to LocalDate (now safe because of the validator)
//            LocalDate start = LocalDate.parse(startStr);
//            LocalDate end = LocalDate.parse(endStr);
//
//            System.out.println("Processing range: " + start + " to " + end);
            break;
          case "calc":
            System.out.println("\n--- Calculating average age... ---");
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
    PromptBuilder waitBuilder = prompt.getPromptBuilder();
    waitBuilder.createConfirmPromp()
            .name("wait")
            .message("Do you want to return to main menu?")
            .addPrompt();
    prompt.prompt(waitBuilder.build());
  }
}
