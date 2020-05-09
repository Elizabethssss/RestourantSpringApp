package com.spring.restaurant.service.mapper;

import com.spring.restaurant.domain.Dish;
import com.spring.restaurant.domain.Lunch;
import com.spring.restaurant.domain.Order;
import com.spring.restaurant.domain.OrderStatus;
import com.spring.restaurant.domain.User;
import com.spring.restaurant.entity.DishEntity;
import com.spring.restaurant.entity.LunchEntity;
import com.spring.restaurant.entity.OrderEntity;
import com.spring.restaurant.entity.OrderStatusEntity;
import com.spring.restaurant.entity.UserEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderMapperTest {
    private static Order ORDER;
    private static OrderEntity ORDER_ENTITY;
    private static final User USER = new User();
    private static final UserEntity USER_ENTITY = new UserEntity();
    private static final Dish DISH = new Dish();
    private static final DishEntity DISH_ENTITY = new DishEntity();
    private static final Lunch LUNCH = new Lunch();
    private static final LunchEntity LUNCH_ENTITY = new LunchEntity();
    private static List<Dish> DISHES;
    private static List<DishEntity> DISH_ENTITIES;
    private static List<Lunch> LUNCHES;
    private static List<LunchEntity> LUNCH_ENTITIES;

    @Mock
    private UserMapper userMapper;
    @Mock
    private DishMapper dishMapper;
    @Mock
    private LunchMapper lunchMapper;
    @InjectMocks
    private OrderMapper orderMapper;

    @Before
    public void setUp() {
        DISHES = new ArrayList<>();
        DISHES.add(DISH);
        DISH_ENTITIES = new ArrayList<>();
        DISH_ENTITIES.add(DISH_ENTITY);
        LUNCHES = new ArrayList<>();
        LUNCHES.add(LUNCH);
        LUNCH_ENTITIES = new ArrayList<>();
        LUNCH_ENTITIES.add(LUNCH_ENTITY);
        ORDER = initOrder();
        ORDER_ENTITY = initOrderEntity();
    }

    @Test
    public void mapEntityToDomain() {
        when(userMapper.mapEntityToDomain(USER_ENTITY)).thenReturn(USER);
        when(dishMapper.mapEntityToDomain(DISH_ENTITY)).thenReturn(DISH);
        when(lunchMapper.mapEntityToDomain(LUNCH_ENTITY)).thenReturn(LUNCH);

        final Order order = orderMapper.mapEntityToDomain(ORDER_ENTITY);

        assertThat(order.getId(), is(ORDER_ENTITY.getId()));
        assertThat(order.getStatus(), is(OrderStatus.PAYED));
        assertThat(order.getCost(), is(ORDER_ENTITY.getCost()));
        assertThat(order.getCreatedAt(), is(ORDER_ENTITY.getCreatedAt()));
        assertThat(order.getUser(), equalTo(USER));
        assertThat(order.getDishes(), equalTo(DISHES));
        assertThat(order.getLunches(), equalTo(LUNCHES));
        verify(userMapper).mapEntityToDomain(USER_ENTITY);
        verify(dishMapper).mapEntityToDomain(DISH_ENTITY);
        verify(lunchMapper).mapEntityToDomain(LUNCH_ENTITY);
    }

    @Test
    public void mapEntityToDomainWithNullDishesAndLunches() {
        when(userMapper.mapEntityToDomain(USER_ENTITY)).thenReturn(USER);
        ORDER_ENTITY.setDishEntities(null);
        ORDER_ENTITY.setLunchEntities(null);

        final Order order = orderMapper.mapEntityToDomain(ORDER_ENTITY);

        assertThat(order.getId(), is(ORDER_ENTITY.getId()));
        assertThat(order.getStatus(), is(OrderStatus.PAYED));
        assertThat(order.getCost(), is(ORDER_ENTITY.getCost()));
        assertThat(order.getCreatedAt(), is(ORDER_ENTITY.getCreatedAt()));
        assertThat(order.getUser(), equalTo(USER));
        assertNull(order.getDishes());
        assertNull(order.getLunches());
        verify(userMapper).mapEntityToDomain(USER_ENTITY);
    }

    @Test
    public void mapEntityToDomainShouldReturnNull() {
        final Order order = orderMapper.mapEntityToDomain(null);
        assertNull(order);
    }

    @Test
    public void mapDomainToEntity() {
        when(userMapper.mapDomainToEntity(USER)).thenReturn(USER_ENTITY);
        when(dishMapper.mapDomainToEntity(DISH)).thenReturn(DISH_ENTITY);
        when(lunchMapper.mapDomainToEntity(LUNCH)).thenReturn(LUNCH_ENTITY);

        final OrderEntity orderEntity = orderMapper.mapDomainToEntity(ORDER);

        assertThat(orderEntity.getId(), is(ORDER.getId()));
        assertThat(orderEntity.getStatus(), is(OrderStatusEntity.PAYED));
        assertThat(orderEntity.getCost(), is(ORDER.getCost()));
        assertThat(orderEntity.getCreatedAt(), is(ORDER.getCreatedAt()));
        assertThat(orderEntity.getUserEntity(), equalTo(USER_ENTITY));
        assertThat(orderEntity.getDishEntities(), equalTo(DISH_ENTITIES));
        assertThat(orderEntity.getLunchEntities(), equalTo(LUNCH_ENTITIES));
        verify(userMapper).mapDomainToEntity(USER);
        verify(dishMapper).mapDomainToEntity(DISH);
        verify(lunchMapper).mapDomainToEntity(LUNCH);
    }

    @Test
    public void mapDomainToEntityWithNullDishesAndLunches() {
        when(userMapper.mapDomainToEntity(USER)).thenReturn(USER_ENTITY);
        ORDER.setDishes(null);
        ORDER.setLunches(null);

        final OrderEntity orderEntity = orderMapper.mapDomainToEntity(ORDER);

        assertThat(orderEntity.getId(), is(ORDER.getId()));
        assertThat(orderEntity.getStatus(), is(OrderStatusEntity.PAYED));
        assertThat(orderEntity.getCost(), is(ORDER.getCost()));
        assertThat(orderEntity.getCreatedAt(), is(ORDER.getCreatedAt()));
        assertThat(orderEntity.getUserEntity(), equalTo(USER_ENTITY));
        assertNull(orderEntity.getDishEntities());
        assertNull(orderEntity.getLunchEntities());
        verify(userMapper).mapDomainToEntity(USER);
    }

    @Test
    public void mapDomainToEntityShouldReturnNull() {
        final OrderEntity orderEntity = orderMapper.mapDomainToEntity(null);
        assertNull(orderEntity);
    }

    private static Order initOrder() {
        final Order order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PAYED);
        order.setCost(100);
        order.setCreatedAt(LocalDate.now());
        order.setUser(USER);
        order.setDishes(DISHES);
        order.setLunches(LUNCHES);
        return order;
    }

    private static OrderEntity initOrderEntity() {
        final OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(1L);
        orderEntity.setStatus(OrderStatusEntity.PAYED);
        orderEntity.setCost(100);
        orderEntity.setCreatedAt(LocalDate.now());
        orderEntity.setUserEntity(USER_ENTITY);
        orderEntity.setDishEntities(DISH_ENTITIES);
        orderEntity.setLunchEntities(LUNCH_ENTITIES);
        return orderEntity;
    }
}