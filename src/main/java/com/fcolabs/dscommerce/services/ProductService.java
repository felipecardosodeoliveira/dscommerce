package com.fcolabs.dscommerce.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.fcolabs.dscommerce.DTO.CategoryDTO;
import com.fcolabs.dscommerce.DTO.ProductDTO;
import com.fcolabs.dscommerce.DTO.ProductMinDTO;
import com.fcolabs.dscommerce.entities.Category;
import com.fcolabs.dscommerce.entities.Product;
import com.fcolabs.dscommerce.repositories.ProductRepository;
import com.fcolabs.dscommerce.services.Exceptions.DatabaseException;
import com.fcolabs.dscommerce.services.Exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    private final CategoryService categoryService;

    ProductService(CategoryService categoryService, ProductRepository productRepository) {
        this.categoryService = categoryService;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public Page<ProductMinDTO> findAll(String name, Pageable pageable) {
        Page<Product> result = productRepository.searchByName(name, pageable);
        Page<ProductMinDTO> pageProductDTO = result
            .map(prod -> new ProductMinDTO(prod));
        return pageProductDTO;
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = productRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        ProductDTO productDTO = new ProductDTO(product);
        return productDTO;
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(@PathVariable Long id, ProductDTO dto) {
        try {
            Product entity = productRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = productRepository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(@PathVariable Long id) {
        try {
            productRepository.deleteById(id);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        } catch(DataIntegrityViolationException e) {
            throw new DatabaseException("Referential Integrity Failed");
        }
    }

    private Product copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
        entity.getCategories().clear();
        for(CategoryDTO categoryDTO: dto.getCategories()) {
            Category category = categoryService.getReference(categoryDTO.getId());
            entity.getCategories().add(category);
        }

        return entity;
    }
}
