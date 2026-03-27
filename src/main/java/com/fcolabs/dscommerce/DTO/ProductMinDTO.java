package com.fcolabs.dscommerce.DTO;

import com.fcolabs.dscommerce.entities.Product;

public class ProductMinDTO {
    private String name;
    private Double price;
    private String imgUrl;
    
    public ProductMinDTO() {
    }

    public ProductMinDTO(String name, Double price, String imgUrl) {
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
    }
    
    public ProductMinDTO(Product entity) {
        this.name = entity.getName();
        this.price = entity.getPrice();
        this.imgUrl = entity.getImgUrl();
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }     
}
