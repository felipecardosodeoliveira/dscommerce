package com.fcolabs.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fcolabs.dscommerce.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    
}
