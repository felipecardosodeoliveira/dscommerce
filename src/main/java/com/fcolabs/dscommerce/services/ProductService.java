package com.fcolabs.dscommerce.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcolabs.dscommerce.DTO.ProductDTO;
import com.fcolabs.dscommerce.entities.Product;
import com.fcolabs.dscommerce.repositories.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        List<Product> result = productRepository.findAll();
        List<ProductDTO> products = result
            .stream()
            .map(product -> new ProductDTO(product))
            .toList();

        return products;
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id).get();
        ProductDTO productDTO = new ProductDTO(product);
        return productDTO;
    }

    public ProductDTO save(ProductDTO productDTO) {
        Product product = new Product(productDTO.getId(), productDTO.getName(), productDTO.getDescription(), productDTO.getPrice(), productDTO.getImgUrl());
        product = productRepository.save(product);
        return new ProductDTO(product);
    }
}
