package com.example.payroll_service;

import com.example.payroll_service.models.Employee;

public class EmployeeNotFoundException extends RuntimeException {

     public EmployeeNotFoundException(Long id) {
        super("Could not find employee " + id);
    }
}
