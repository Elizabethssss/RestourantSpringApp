package com.spring.restaurant.service.mapper;

import com.spring.restaurant.domain.Order;
import com.spring.restaurant.domain.OrderStatus;
import com.spring.restaurant.entity.OrderEntity;
import com.spring.restaurant.entity.OrderStatusEntity;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrderMapper implements Mapper<Order, OrderEntity> {

    private final UserMapper userMapper;
    private final DishMapper dishMapper;
    private final LunchMapper lunchMapper;

    @Override
    public Order mapEntityToDomain(OrderEntity entity) {
        if (entity == null) {
            return null;
        }
        Order order = new Order();
        order.setId(entity.getId());
        order.setStatus(OrderStatus.valueOf(entity.getStatus().name()));
        order.setCost(entity.getCost());
        order.setCreatedAt(entity.getCreatedAt());
        order.setUser(userMapper.mapEntityToDomain(entity.getUserEntity()));
        order.setDishes(entity.getDishEntities() == null ? null : entity.getDishEntities().stream()
                .map(dishMapper::mapEntityToDomain).collect(Collectors.toList()));
        order.setLunches(entity.getLunchEntities() == null ? null : entity.getLunchEntities().stream()
                .map(lunchMapper::mapEntityToDomain).collect(Collectors.toList()));
        return order;
    }

    @Override
    public OrderEntity mapDomainToEntity(Order domain) {
        if (domain == null) {
            return null;
        }
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(domain.getId());
        orderEntity.setStatus(OrderStatusEntity.valueOf(domain.getStatus().name()));
        orderEntity.setCost(domain.getCost());
        orderEntity.setCreatedAt(domain.getCreatedAt());
        orderEntity.setUserEntity(userMapper.mapDomainToEntity(domain.getUser()));
        orderEntity.setDishEntities(domain.getDishes() == null ? null : domain.getDishes().stream()
                .map(dishMapper::mapDomainToEntity).collect(Collectors.toList()));
        orderEntity.setLunchEntities(domain.getLunches() == null ? null : domain.getLunches().stream()
                .map(lunchMapper::mapDomainToEntity).collect(Collectors.toList()));
        return orderEntity;
    }
}
