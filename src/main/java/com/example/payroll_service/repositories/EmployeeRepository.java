package com.example.payroll_service.repositories;


import com.example.payroll_service.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
