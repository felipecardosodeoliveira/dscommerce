package com.fcolabs.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.fcolabs.dscommerce.DTO.CategoryDTO;
import com.fcolabs.dscommerce.DTO.ProductDTO;
import com.fcolabs.dscommerce.entities.Category;
import com.fcolabs.dscommerce.entities.Product;
import com.fcolabs.dscommerce.repositories.CategoryRepository;
import com.fcolabs.dscommerce.services.Exceptions.DatabaseException;
import com.fcolabs.dscommerce.services.Exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

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

    @Transactional
    public CategoryDTO insert(CategoryDTO dto) {
        Category entity = new Category();
        copyDtoToEntity(dto, entity);
        entity = categoryRepository.save(entity);
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryDTO update(@PathVariable Long id, CategoryDTO dto) {
        try {
            Category entity = categoryRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = categoryRepository.save(entity);
            return new CategoryDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(@PathVariable Long id) {
        try {
            categoryRepository.deleteById(id);            
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        } catch(DataIntegrityViolationException e) {
            throw new DatabaseException("Referential Integrity Failed");
        }
    }

    private Category copyDtoToEntity(CategoryDTO dto, Category entity) {
        entity.setName(dto.getName());
        return entity;
    }
}
