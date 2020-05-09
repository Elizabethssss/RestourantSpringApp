package com.spring.restaurant.service.impl;

import com.spring.restaurant.domain.Dish;
import com.spring.restaurant.domain.DishType;
import com.spring.restaurant.entity.DishEntity;
import com.spring.restaurant.entity.DishTypeEntity;
import com.spring.restaurant.repository.DishRepository;
import com.spring.restaurant.service.mapper.DishMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DishServiceImplTest {
    private static final Dish DISH = initDish();
    private static final DishEntity DISH_ENTITY = initDishEntity();

    @Mock
    private DishRepository dishRepository;
    @Mock
    private DishMapper dishMapper;
    @InjectMocks
    private DishServiceImpl dishService;

    @Test
    public void pageCount() {
        when(dishRepository.countAllByDishTypeEntity(DISH_ENTITY.getDishTypeEntity())).thenReturn(100L);

        int pages = dishService.pageCount(DISH.getDishType());

        assertTrue(pages > 0);
        verify(dishRepository).countAllByDishTypeEntity(DISH_ENTITY.getDishTypeEntity());
    }

    @Test
    public void findAllByPageAndDishType() {
        Pageable pageable = PageRequest.of(3, 9);
        when(dishRepository.findAllByDishTypeEntity(DISH_ENTITY.getDishTypeEntity(), pageable))
                .thenReturn(Page.empty());

        Page<Dish> dishPage = dishService.findAllByPageAndDishType(3, DISH.getDishType());

        assertEquals(dishPage, Page.empty());
        verify(dishRepository).findAllByDishTypeEntity(DISH_ENTITY.getDishTypeEntity(), pageable);
    }

    @Test
    public void findById() {
        when(dishRepository.findById(DISH.getId())).thenReturn(Optional.of(DISH_ENTITY));
        when(dishMapper.mapEntityToDomain(DISH_ENTITY)).thenReturn(DISH);

        Optional<Dish> dish = dishService.findById(DISH.getId());

        assertEquals(Optional.of(DISH), dish);
        verify(dishRepository).findById(DISH.getId());
        verify(dishMapper).mapEntityToDomain(DISH_ENTITY);
    }

    private static Dish initDish() {
        final Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Dish name");
        dish.setAbout("About dish");
        dish.setDishType(DishType.DESSERT);
        dish.setPrice(100);
        dish.setWeight(100);
        dish.setImg("Image");
//        dish.setIngredients(INGREDIENTS);
        return dish;
    }

    private static DishEntity initDishEntity() {
        final DishEntity dishEntity = new DishEntity();
        dishEntity.setId(1L);
        dishEntity.setName("Dish name");
        dishEntity.setAbout("About dish");
        dishEntity.setDishTypeEntity(DishTypeEntity.DESSERT);
        dishEntity.setPrice(100);
        dishEntity.setWeight(100);
        dishEntity.setImg("Image");
//        dishEntity.setIngredientEntities(INGREDIENT_ENTITIES);
        return dishEntity;
    }
}