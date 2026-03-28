package com.fcolabs.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fcolabs.dscommerce.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    
} 
