//package com.spring.restaurant.service.impl;
//
//import com.spring.restaurant.domain.Order;
//import com.spring.restaurant.domain.OrderStatus;
//import com.spring.restaurant.domain.Role;
//import com.spring.restaurant.domain.User;
//import com.spring.restaurant.entity.OrderEntity;
//import com.spring.restaurant.entity.OrderStatusEntity;
//import com.spring.restaurant.entity.RoleEntity;
//import com.spring.restaurant.entity.UserEntity;
//import com.spring.restaurant.repository.OrderRepository;
//import com.spring.restaurant.service.mapper.Mapper;
//import com.spring.restaurant.service.mapper.OrderMapper;
//import com.spring.restaurant.service.mapper.UserMapper;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.Assert.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class OrderServiceImplTest {
//    private static Order ORDER;
//    private static OrderEntity ORDER_ENTITY;
//    private static User USER;
//    private static UserEntity USER_ENTITY;
//
//    @Mock
//    private OrderRepository orderRepository;
//    @Mock
//    private UserMapper userMapper;
//    @Mock
//    private OrderMapper orderMapper;
//    @InjectMocks
//    private OrderServiceImpl orderService;
//
//    @Before
//    public void setUp() {
//        ORDER = initOrder();
//        ORDER_ENTITY = initOrderEntity();
//        USER = getUser();
//        USER_ENTITY = getUserEntity();
//        ORDER.setUser(USER);
//        ORDER_ENTITY.setUserEntity(USER_ENTITY);
//    }
//
//
//    @After
//    public void reset() {
//        Mockito.reset(orderRepository, orderMapper, userMapper, orderService);
//    }
//
//    @Test
//    public void saveOrder() {
//        when(orderRepository.save(any())).thenReturn(ORDER_ENTITY);
//        when(orderMapper.mapDomainToEntity(ORDER)).thenReturn(ORDER_ENTITY);
//
//        orderService.saveOrder(ORDER);
//
//        verify(orderRepository).save(any());
//        verify(orderMapper).mapDomainToEntity(ORDER);
//    }
//
//    @Test
//     public void findByUserEntityAndStatus() {
//        when(userMapper.mapDomainToEntity(USER)).thenReturn(USER_ENTITY);
//        when(orderRepository.findByUserEntityAndStatus(USER_ENTITY, ORDER_ENTITY.getStatus())).thenReturn(Optional.of(ORDER_ENTITY));
//        when(orderMapper.mapEntityToDomain(ORDER_ENTITY)).thenReturn(ORDER);
//
//        Optional<Order> order = orderService.findByUserEntityAndStatus(USER, ORDER.getStatus());
//
//        assertEquals(Optional.of(ORDER), order);
//        verify(userMapper).mapDomainToEntity(USER);
//        verify(orderMapper).mapEntityToDomain(ORDER_ENTITY);
//        verify(orderRepository).findByUserEntityAndStatus(USER_ENTITY, ORDER_ENTITY.getStatus());
//    }
//
////    @Test
////    public void pageCount() {
////        when(orderRepository.countAllByUserEntity(any())).thenReturn(100L);
////
////        int pages = orderService.pageCount(USER);
////
////        assertTrue(pages > 0);
////        verify(orderRepository).countAllByUserEntity(any());
////    }
////
//////    @Test
//////    public void findAllByUserAndStatusNot() {
//////        Pageable pageable = PageRequest.of(3, 7);
//////        when(orderRepository.findAllByUserEntityAndStatusNot(USER_ENTITY,
//////                ORDER_ENTITY.getStatus(), pageable)).thenReturn(Page.empty());
//////        when(userMapper.mapDomainToEntity(any())).thenReturn(USER_ENTITY);
//////        when(orderMapper.mapEntityToDomain(ORDER_ENTITY)).thenReturn(ORDER);
//////
//////        Page<Order> orderPage = orderService.findAllByUserAndStatusNot(USER, ORDER.getStatus(), 3);
//////
//////        assertEquals(orderPage, Page.empty());
//////        verify(userMapper).mapDomainToEntity(any());
//////        verify(orderMapper).mapEntityToDomain(ORDER_ENTITY);
//////        verify(orderRepository).findAllByUserEntityAndStatusNot(USER_ENTITY,
//////                ORDER_ENTITY.getStatus(), pageable);
//////    }
////
////    @Test
////    public void pageCountByStatus() {
////        when(orderRepository.countAllByStatus(ORDER_ENTITY.getStatus())).thenReturn(100L);
////
////        int pages = orderService.pageCountByStatus(ORDER.getStatus());
////
////        assertTrue(pages > 0);
////        verify(orderRepository).countAllByStatus(ORDER_ENTITY.getStatus());
////    }
////
////    @Test
////    public void findAllByStatus() {
////        Pageable pageable = PageRequest.of(3, 7);
////        when(orderRepository.findAllByStatus(ORDER_ENTITY.getStatus(), pageable)).thenReturn(Page.empty());
////
////        Page<Order> orderPage = orderService.findAllByStatus(ORDER.getStatus(), 3);
////
////        assertEquals(orderPage, Page.empty());
////        verify(orderRepository).findAllByStatus(ORDER_ENTITY.getStatus(), pageable);
////    }
////
////    @Test
////    public void findById() {
////        when(orderRepository.findById(ORDER.getId())).thenReturn(Optional.of(ORDER_ENTITY));
////        when(orderMapper.mapEntityToDomain(ORDER_ENTITY)).thenReturn(ORDER);
////
////        Optional<Order> order = orderService.findById(ORDER.getId());
////
////        assertEquals(Optional.of(ORDER), order);
////        verify(orderRepository).findById(ORDER.getId());
////        verify(orderMapper).mapEntityToDomain(ORDER_ENTITY);
////    }
//
//    private static Order initOrder() {
//        final Order order = new Order();
//        order.setId(1L);
//        order.setStatus(OrderStatus.PAYED);
//        order.setCost(100);
//        order.setCreatedAt(LocalDate.now());
//        return order;
//    }
//
//    private static OrderEntity initOrderEntity() {
//        final OrderEntity orderEntity = new OrderEntity();
//        orderEntity.setId(1L);
//        orderEntity.setStatus(OrderStatusEntity.PAYED);
//        orderEntity.setCost(100);
//        orderEntity.setCreatedAt(LocalDate.now());
//        return orderEntity;
//    }
//
//    private static User getUser() {
//        final User user = new User();
//        user.setId(1L);
//        user.setUsername("Username");
//        user.setEmail("Email");
//        user.setPassword("Password");
//        user.setRole(Role.USER);
//        List<Order> orders = new ArrayList<>();
//        orders.add(ORDER);
//        orders.add(ORDER);
//        user.setOrders(orders);
//        return user;
//    }
//
//    private static UserEntity getUserEntity() {
//        final UserEntity userEntity = new UserEntity();
//        userEntity.setId(1L);
//        userEntity.setUsername("Username");
//        userEntity.setEmail("Email");
//        userEntity.setPassword("Password");
//        userEntity.setRoleEntity(RoleEntity.USER);
//        List<OrderEntity> orderEntities = new ArrayList<>();
//        orderEntities.add(ORDER_ENTITY);
//        orderEntities.add(ORDER_ENTITY);
//        userEntity.setOrderEntities(orderEntities);
//        return userEntity;
//    }
//}