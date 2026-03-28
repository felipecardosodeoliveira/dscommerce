package com.fcolabs.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fcolabs.dscommerce.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    
} 
