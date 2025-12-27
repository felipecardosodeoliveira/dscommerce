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
import com.fcolabs.dscommerce.services.Exceptions.EntityNotFound;

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
        Product product = productRepository.findById(id).get();
        ProductDTO productDTO = new ProductDTO(product);
        return productDTO;
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        Product product = copyDtoToProduct(dto);
        product = productRepository.save(product);
        return new ProductDTO(product);
    }

    @Transactional
    public ProductDTO update(@PathVariable Long id, ProductDTO dto) {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFound("Product not found with id: " + id));
            product = copyDtoToProduct(dto);
            Product updatedProduct = productRepository.save(product);
            return new ProductDTO(updatedProduct);
    }

    @Transactional
    public void delete(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new EntityNotFound("Product not found with id: " + id));
        productRepository.delete(product);
    }

    private Product copyDtoToProduct(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImgUrl(dto.getImgUrl());
        return product;
    }
}
