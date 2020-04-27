package com.spring.restaurant.service;

import com.spring.restaurant.domain.Dish;
import com.spring.restaurant.domain.DishType;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface DishService {
    int pageCount(DishType dishType);

    Page<Dish> findAllByPageAndDishType(int pageNumber, DishType dishType);

    Optional<Dish> findById(Long dishId);
}
