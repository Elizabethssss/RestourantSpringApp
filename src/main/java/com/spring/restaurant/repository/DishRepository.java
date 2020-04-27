package com.spring.restaurant.repository;

import com.spring.restaurant.entity.DishEntity;
import com.spring.restaurant.entity.DishTypeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DishRepository extends JpaRepository<DishEntity, Long> {
    Page<DishEntity> findAllByDishTypeEntity(DishTypeEntity dishTypeEntity, Pageable pageable);
    long countAllByDishTypeEntity(DishTypeEntity dishTypeEntity);
}
