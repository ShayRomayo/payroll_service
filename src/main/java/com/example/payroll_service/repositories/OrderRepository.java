package com.example.payroll_service.repositories;

import com.example.payroll_service.models.Order;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
