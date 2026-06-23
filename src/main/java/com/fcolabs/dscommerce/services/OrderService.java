package com.fcolabs.dscommerce.services;

import java.time.Instant;

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
    
    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final UserService userService;

    OrderService(ProductRepository productRepository, UserRepository userRepository, OrderItemRepository orderItemRepository, OrderRepository orderRepository, UserService userService) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

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

        User user = userService.authenticated();

        order.setClient(user);

        for (OrderItemDTO item: dto.getItems()) {
            Product product = productRepository.getReferenceById(item.getProductId());
            OrderItem orderItem = new OrderItem(order, product, item.getQuantity(), product.getPrice());
            order.getItems().add(orderItem);
        }

        orderRepository.save(order);
        orderItemRepository.saveAll(order.getItems());
        return new OrderDTO(order);
    }
}
