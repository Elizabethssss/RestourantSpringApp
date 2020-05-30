package com.spring.restaurant.service.impl;

import com.spring.restaurant.domain.Order;
import com.spring.restaurant.domain.OrderStatus;
import com.spring.restaurant.domain.User;
import com.spring.restaurant.entity.OrderEntity;
import com.spring.restaurant.entity.OrderStatusEntity;
import com.spring.restaurant.entity.UserEntity;
import com.spring.restaurant.repository.OrderRepository;
import com.spring.restaurant.service.OrderService;
import com.spring.restaurant.service.mapper.Mapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.spring.restaurant.service.util.ServiceUtil.getNumberOfPages;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceImpl implements OrderService {
    private static final int ITEMS_PER_PAGE = 7;
    private final OrderRepository orderRepository;
    private final Mapper<User, UserEntity> userMapper;
    private final Mapper<Order, OrderEntity> orderMapper;

    @Override
    public void saveOrder(Order order) {
        orderRepository.save(orderMapper.mapDomainToEntity(order));
    }

    @Override
    public Optional<Order> findByUserEntityAndStatus(User user, OrderStatus orderStatus) {
        return orderRepository.findByUserEntityAndStatus(userMapper.mapDomainToEntity(user),
                OrderStatusEntity.valueOf(orderStatus.name())).map(orderMapper::mapEntityToDomain);
    }

    @Override
    public int pageCount(User user) {
        return getNumberOfPages(orderRepository.countAllByUserEntity(userMapper.mapDomainToEntity(user)), ITEMS_PER_PAGE);
    }

    @Override
    public Page<Order> findAllByUserAndStatusNot(User user, OrderStatus orderStatus, int pageNumber) {
        Pageable orderPage = PageRequest.of(pageNumber, ITEMS_PER_PAGE);
        return orderRepository.findAllByUserEntityAndStatusNot(userMapper.mapDomainToEntity(user),
                OrderStatusEntity.valueOf(orderStatus.name()), orderPage).map(orderMapper::mapEntityToDomain);
    }

    @Override
    public int pageCountByStatus(OrderStatus orderStatus) {
        return getNumberOfPages(orderRepository.countAllByStatus(OrderStatusEntity.valueOf(orderStatus.name())), ITEMS_PER_PAGE);
    }

    @Override
    public Page<Order> findAllByStatus(OrderStatus orderStatus, int pageNumber) {
        Pageable orderPage = PageRequest.of(pageNumber, ITEMS_PER_PAGE);
        return orderRepository.findAllByStatus(OrderStatusEntity.valueOf(orderStatus.name()), orderPage).map(orderMapper::mapEntityToDomain);
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderRepository.findById(orderId).map(orderMapper::mapEntityToDomain);
    }

}
