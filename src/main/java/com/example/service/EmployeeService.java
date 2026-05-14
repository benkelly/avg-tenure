package com.example.service;

import com.example.configuration.JsonEmployeeReader;
import com.example.model.Employee;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class EmployeeService {

  List<Employee> employeeList = null;

  public EmployeeService() {
    this.employeeList = JsonEmployeeReader.loadEmployees();;
  }

  public String calculateAverageAge() {
    if (employeeList == null || employeeList.isEmpty()) {
      return "0";
    }
    double totalAge = employeeList.stream()
            .mapToDouble(e -> Period.between(e.getDate_of_birth(), LocalDate.now()).getYears())
            .average()
            .orElse(0.0);
    return String.format("%.2f", totalAge);
  }

  public void displayEmployeeTenureYearsTable() {
    AsciiTable asciiTable = new AsciiTable();
    asciiTable.addRule();
    asciiTable.addRow("ID", "First Name", "Last Name", "Tenure (Years)", "Current Employee");
    asciiTable.addRule();
    employeeList.forEach(employee -> {
      asciiTable.addRow(
              Objects.toString(employee.getId(), ""),
              Objects.toString(employee.getFirst_name(), ""),
              Objects.toString(employee.getLast_name(), ""),
              String.format("%.2f", employee.getTenureYears()),
              Objects.toString(isCurrentEmployee(employee), ""));
      asciiTable.addRule();
    });
    asciiTable.setTextAlignment(TextAlignment.CENTER);
    String render = asciiTable.render();
    System.out.println(render);
  }

  public void displayEmployeesByTenureFilter(LocalDate start, LocalDate end) {
    AsciiTable asciiTable = new AsciiTable();
    asciiTable.addRule();
    asciiTable.addRow("ID", "First Name", "Last Name", "Hire Date", "End Date");
    asciiTable.addRule();
    employeeList.stream()
            .filter(e -> {
              LocalDate hire = e.getHire_date();
              return (hire.isAfter(start) || hire.isEqual(start)) && (hire.isBefore(end) || hire.isEqual(end));
            })
            .forEach(employee -> {
              asciiTable.addRow(
                      Objects.toString(employee.getId(), ""),
                      Objects.toString(employee.getFirst_name(), ""),
                      Objects.toString(employee.getLast_name(), ""),
                      Objects.toString(employee.getHire_date(), ""),
                      Objects.toString(employee.getEnd_date(), ""));
              asciiTable.addRule();
            });
    asciiTable.setTextAlignment(TextAlignment.CENTER);
    String render = asciiTable.render();
    System.out.println(render);
  }

  public void displayEmployeeTable() {
    AsciiTable asciiTable = new AsciiTable();
    asciiTable.addRule();
    asciiTable.addRow("ID", "First Name", "Last Name", "DOB", "Hire Date", "End Date");
    asciiTable.addRule();
    employeeList.forEach(employee -> {
      asciiTable.addRow(
              Objects.toString(employee.getId(), ""),
              Objects.toString(employee.getFirst_name(), ""),
              Objects.toString(employee.getLast_name(), ""),
              Objects.toString(employee.getDate_of_birth(), ""),
              Objects.toString(employee.getHire_date(), ""),
              Objects.toString(employee.getEnd_date(), ""));
      asciiTable.addRule();
    });
    asciiTable.setTextAlignment(TextAlignment.CENTER);
    String render = asciiTable.render();
    System.out.println(render);
  }

  public void displayEmployeeTenureTable() {
    AsciiTable asciiTable = new AsciiTable();
    asciiTable.addRule();
    asciiTable.addRow("ID", "First Name", "Last Name", "Days Employed", "Employment Duration", "Current Employee");
    asciiTable.addRule();
    employeeList.forEach(employee -> {
      asciiTable.addRow(
              Objects.toString(employee.getId(), ""),
              Objects.toString(employee.getFirst_name(), ""),
              Objects.toString(employee.getLast_name(), ""),
              Objects.toString(calcNumberOfDaysEmployed(employee), ""),
              Objects.toString(calcPeriodEmployed(employee), ""),
              Objects.toString(isCurrentEmployee(employee), ""));
      asciiTable.addRule();
    });
    asciiTable.setTextAlignment(TextAlignment.CENTER);
    String render = asciiTable.render();
    System.out.println(render);
  }

  private String calcPeriodEmployed(Employee employee) {
    LocalDate startDate = employee.getHire_date();
    LocalDate endDate = employee.getEnd_date() != null ? employee.getEnd_date() : LocalDate.now();
    Period period = Period.between(startDate, endDate);
    String result = String.format("%s years %s months %s days", period.getYears(), period.getMonths(), period.getDays());
    return result;
  }

  private String isCurrentEmployee(Employee employee) {
    return employee.getEnd_date() == null ? "Yes" : "No";
  }

  private long calcNumberOfDaysEmployed(Employee employee){
    LocalDate startDate = employee.getHire_date();
    LocalDate endDate = employee.getEnd_date() != null ? employee.getEnd_date() : LocalDate.now();
    long result = ChronoUnit.DAYS.between(startDate, endDate);
    return result;
  }
}
