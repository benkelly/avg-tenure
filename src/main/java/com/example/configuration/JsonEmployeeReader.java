package com.example.configuration;

import com.example.model.Employee;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class JsonEmployeeReader {
    private static final String FILE_PATH = "/employeeData.json";

    public static List<Employee> loadEmployees() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        try (InputStream inputStream = JsonEmployeeReader.class.getResourceAsStream(FILE_PATH)){
            return mapper.readValue(inputStream, new TypeReference<List<Employee>>() {});
        } catch (java.io.IOException e) {
            System.err.println("Error reading employee data: " + e.getMessage());
            return Collections.emptyList();
        }
    }

}
