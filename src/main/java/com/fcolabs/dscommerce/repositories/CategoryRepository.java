package com.fcolabs.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fcolabs.dscommerce.entities.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}
