package com.example.model;

import java.time.LocalDate;

public class Employee {
    private long id;
    private String first_name;
    private String last_name;
    private LocalDate date_of_birth;
    private LocalDate hire_date;
    private LocalDate end_date;

    public Employee() {
    }

    public Employee(long id, String first_name, String last_name, LocalDate date_of_birth, LocalDate hire_date, LocalDate end_date) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.date_of_birth = date_of_birth;
        this.hire_date = hire_date;
        this.end_date = end_date;
    }

    public Employee(long id, String first_name, String last_name, LocalDate date_of_birth, LocalDate hire_date) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.date_of_birth = date_of_birth;
        this.hire_date = hire_date;
        this.end_date = null;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public LocalDate getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(LocalDate date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public LocalDate getHire_date() {
        return hire_date;
    }

    public void setHire_date(LocalDate hire_date) {
        this.hire_date = hire_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    public long getTenureDays() {
        LocalDate effectiveEndDate = (end_date != null) ? end_date : LocalDate.now();
        return java.time.temporal.ChronoUnit.DAYS.between(hire_date, effectiveEndDate);
    }

    public double getTenureYears() {
        return getTenureDays() / 365.25;
    }

}
