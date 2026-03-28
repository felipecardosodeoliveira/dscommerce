package com.fcolabs.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fcolabs.dscommerce.entities.OrderItem;
import com.fcolabs.dscommerce.entities.OrderItemPK;

public interface OrderItemRepository extends JpaRepository<OrderItem, OrderItemPK> {
    
}
