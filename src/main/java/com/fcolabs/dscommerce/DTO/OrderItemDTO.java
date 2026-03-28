package com.fcolabs.dscommerce.DTO;

import com.fcolabs.dscommerce.entities.OrderItem;

public class OrderItemDTO {
    private Integer quantity;
    private Double price;
    private Long productId;
    private String imgUrl;

    public OrderItemDTO() {

    }

    public OrderItemDTO(Integer quantity, Double price, Long productId, String imString) {
        this.quantity = quantity;
        this.price = price;
        this.productId = productId;
        this.imgUrl = imString;
    }

    public OrderItemDTO(OrderItem entity) {
        quantity = entity.getQuantity();
        price = entity.getPrice();
        productId = entity.geProduct().getId();
        imgUrl = entity.geProduct().getImgUrl();
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getPrice() {
        return price;
    }

    public Long getProductId() {
        return productId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Double subTotal() {
        return quantity * price;
    }
}
