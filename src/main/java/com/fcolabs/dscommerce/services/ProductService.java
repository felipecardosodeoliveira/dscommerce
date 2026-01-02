package com.fcolabs.dscommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import com.fcolabs.dscommerce.DTO.ProductDTO;
import com.fcolabs.dscommerce.entities.Product;
import com.fcolabs.dscommerce.repositories.ProductRepository;
import com.fcolabs.dscommerce.services.Exceptions.ResourceNotFoundException;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(Pageable pageable) {
        Page<Product> result = productRepository.findAll(pageable);
        Page<ProductDTO> pageProductDTO = result
            .map(prod -> new ProductDTO(prod));
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
        entity = copyDtoToEntity(dto, entity);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(@PathVariable Long id, ProductDTO dto) {
        Product entity = productRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        entity = copyDtoToEntity(dto, entity);
        Product updatedProduct = productRepository.save(entity);
        return new ProductDTO(updatedProduct);
    }

    @Transactional
    public void delete(@PathVariable Long id) {
        Product product = productRepository
            .findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        productRepository.delete(product);
    }

    private Product copyDtoToEntity(ProductDTO dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setPrice(dto.getPrice());
        entity.setImgUrl(dto.getImgUrl());
        return entity;
    }
}
