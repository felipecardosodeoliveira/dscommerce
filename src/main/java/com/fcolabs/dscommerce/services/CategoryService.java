package com.fcolabs.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcolabs.dscommerce.DTO.CategoryDTO;
import com.fcolabs.dscommerce.entities.Category;
import com.fcolabs.dscommerce.repositories.CategoryRepository;
import com.fcolabs.dscommerce.services.Exceptions.ResourceNotFoundException;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Category category = categoryRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        CategoryDTO categoryDTO = new CategoryDTO(category);
        return categoryDTO;
    }
}
