package com.spring.restaurant.repository;

import com.spring.restaurant.entity.OrderEntity;
import com.spring.restaurant.entity.OrderStatusEntity;
import com.spring.restaurant.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByUserEntityAndStatus(UserEntity userEntity, OrderStatusEntity orderStatusEntity);
    long countAllByUserEntity(UserEntity userEntity);
    Page<OrderEntity> findAllByUserEntityAndStatusNot(UserEntity userEntity,
                                                      OrderStatusEntity orderStatusEntity, Pageable orderPage);

    long countAllByStatus(OrderStatusEntity orderStatusEntity);
    Page<OrderEntity> findAllByStatus(OrderStatusEntity orderStatusEntity, Pageable orderPage);
}


