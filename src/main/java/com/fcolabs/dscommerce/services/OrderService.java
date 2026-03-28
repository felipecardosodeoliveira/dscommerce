package com.fcolabs.dscommerce.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fcolabs.dscommerce.DTO.OrderDTO;
import com.fcolabs.dscommerce.DTO.OrderItemDTO;
import com.fcolabs.dscommerce.entities.Order;
import com.fcolabs.dscommerce.entities.OrderItem;
import com.fcolabs.dscommerce.entities.Product;
import com.fcolabs.dscommerce.entities.User;
import com.fcolabs.dscommerce.enums.OrderStatus;
import com.fcolabs.dscommerce.repositories.OrderItemRepository;
import com.fcolabs.dscommerce.repositories.OrderRepository;
import com.fcolabs.dscommerce.repositories.ProductRepository;
import com.fcolabs.dscommerce.repositories.UserRepository;
import com.fcolabs.dscommerce.services.Exceptions.ResourceNotFoundException;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;

    @Autowired 
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public OrderDTO findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Recurso não encontrado!"));
        return new OrderDTO(order);
    }

    @Transactional
    public OrderDTO insert(OrderDTO dto) {
        Order order = new Order();

        order.setMoment(Instant.now());
        order.setStatus(OrderStatus.WAITING_PAYMENT);

        User user = userRepository.getReferenceById(dto.getClient().getId());
        order.setClient(user);

        for (OrderItemDTO item: dto.getItems()) {
            Product product = productRepository.getReferenceById(item.getProductId());
            OrderItem orderItem = new OrderItem(order, product, item.getQuantity(), item.getPrice());
            order.getItems().add(orderItem);
        }

        orderRepository.save(order);
        orderItemRepository.saveAll(order.getItems());
        return new OrderDTO(order);
    }
}
