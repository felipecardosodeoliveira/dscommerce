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

    public ProductDTO save(ProductDTO productDTO) {
        Product product = new Product(productDTO.getId(), 
            productDTO.getName(),
            productDTO.getDescription(),
            productDTO.getPrice(),
            productDTO.getImgUrl());

        product = productRepository.save(product);
        return new ProductDTO(product);
    }

    public ProductDTO update(@PathVariable Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFound("Product not found with id: " + id));

            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setPrice(productDTO.getPrice());
            product.setImgUrl(productDTO.getImgUrl());

            Product updatedProduct = productRepository.save(product);
            return new ProductDTO(updatedProduct);
    }

    public void delete(@PathVariable Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new EntityNotFound("Product not found with id: " + id));
        productRepository.delete(product);
    }
}
