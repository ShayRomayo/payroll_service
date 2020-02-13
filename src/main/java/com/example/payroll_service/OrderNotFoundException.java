package com.example.payroll_service;

import com.example.payroll_service.models.Employee;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(Long id) {
        super("Could not find employee " + id);
    }
}
