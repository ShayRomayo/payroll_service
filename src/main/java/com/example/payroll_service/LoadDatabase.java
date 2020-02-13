package com.example.payroll_service;

import com.example.payroll_service.models.Employee;
import com.example.payroll_service.models.Order;
import com.example.payroll_service.repositories.EmployeeRepository;

import com.example.payroll_service.repositories.OrderRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(EmployeeRepository repository, OrderRepository orderRepository) {
        return args ->{
            log.info("Preloading " + repository.save(new Employee("Bilbo",  "Baggins", "burglar")));
            log.info("Preloading " + repository.save(new Employee("Frodo", "Baggins", "thief")));
            orderRepository.save(new Order("MacBook Pro", Status.COMPLETED));
            orderRepository.save(new Order("iPhone", Status.IN_PROGRESS));

            orderRepository.findAll().forEach(order -> {
                log.info("Preloaded " + order);
            });
        };
    }
}
