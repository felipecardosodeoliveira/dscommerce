package com.fcolabs.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<CategoryDTO> findAll(Pageable pageable) {
        Page<Category> result = categoryRepository.findAll(pageable);
        Page<CategoryDTO> categoryDTO = result
            .map(cat -> new CategoryDTO(cat));
        return categoryDTO;
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Category category = categoryRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
        CategoryDTO categoryDTO = new CategoryDTO(category);
        return categoryDTO;
    }
}
