package com.example;

import com.example.configuration.MenuBuilder;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {
    System.out.println("Employee Management System Started");
    MenuBuilder.buildMenu();
  }
}
