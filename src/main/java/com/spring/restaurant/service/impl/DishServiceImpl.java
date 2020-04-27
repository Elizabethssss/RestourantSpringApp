package com.spring.restaurant.service.impl;

import com.spring.restaurant.domain.Dish;
import com.spring.restaurant.domain.DishType;
import com.spring.restaurant.entity.DishEntity;
import com.spring.restaurant.entity.DishTypeEntity;
import com.spring.restaurant.repository.DishRepository;
import com.spring.restaurant.service.DishService;
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
public class DishServiceImpl implements DishService {
    private static final int ITEMS_PER_PAGE = 9;
    private final DishRepository dishRepository;
    private final Mapper<Dish, DishEntity> dishMapper;

    @Override
    public int pageCount(DishType dishType) {
        return getNumberOfPages(dishRepository.countAllByDishTypeEntity(DishTypeEntity.valueOf(dishType.name())), ITEMS_PER_PAGE);
    }

    @Override
    public Page<Dish> findAllByPageAndDishType(int pageNumber, DishType dishType) {
        Pageable dishPage = PageRequest.of(pageNumber, ITEMS_PER_PAGE);
        return dishRepository.findAllByDishTypeEntity(DishTypeEntity.valueOf(dishType.name()), dishPage)
                .map(dishMapper::mapEntityToDomain);
    }

    @Override
    public Optional<Dish> findById(Long dishId) {
        return dishRepository.findById(dishId).map(dishMapper::mapEntityToDomain);
    }
}