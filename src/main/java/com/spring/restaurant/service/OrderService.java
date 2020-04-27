package com.spring.restaurant.service;

import com.spring.restaurant.domain.Order;
import com.spring.restaurant.domain.OrderStatus;
import com.spring.restaurant.domain.User;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface OrderService {
    void saveOrder(Order order);

    Optional<Order> findByUserEntityAndStatus(User user, OrderStatus orderStatus);

    int pageCount(User user);

    Page<Order> findAllByUserAndStatusNot(User user, OrderStatus orderStatus, int pageNumber);

    int pageCountByStatus(OrderStatus orderStatus);

    Page<Order> findAllByStatus(OrderStatus orderStatus, int pageNumber);

    Optional<Order> findById(Long orderId);
}
