package com.spring.restaurant.service.mapper;

import com.spring.restaurant.domain.Dish;
import com.spring.restaurant.domain.Lunch;
import com.spring.restaurant.domain.LunchType;
import com.spring.restaurant.entity.DishEntity;
import com.spring.restaurant.entity.LunchEntity;
import com.spring.restaurant.entity.LunchTypeEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LunchMapperTest {
    private static Lunch LUNCH;
    private static LunchEntity LUNCH_ENTITY ;
    private static final Dish DISH = new Dish();
    private static final DishEntity DISH_ENTITY = new DishEntity();
    private static List<Dish> DISHES;
    private static List<DishEntity> DISH_ENTITIES;

    @Mock
    private DishMapper dishMapper;
    @InjectMocks
    private LunchMapper lunchMapper;

    @Before
    public void setUp() {
        DISHES = new ArrayList<>();
        DISHES.add(DISH);
        DISH_ENTITIES = new ArrayList<>();
        DISH_ENTITIES.add(DISH_ENTITY);
        LUNCH = initLunch();
        LUNCH_ENTITY = initLunchEntity();
    }

    @Test
    public void mapEntityToDomain() {
        when(dishMapper.mapEntityToDomain(DISH_ENTITY)).thenReturn(DISH);

        final Lunch lunch = lunchMapper.mapEntityToDomain(LUNCH_ENTITY);

        assertThat(lunch.getId(), is(LUNCH_ENTITY.getId()));
        assertThat(lunch.getName(), is(LUNCH_ENTITY.getName()));
        assertThat(lunch.getDescription(), is(LUNCH_ENTITY.getDescription()));
        assertThat(lunch.getImg(), is(LUNCH_ENTITY.getImg()));
        assertThat(lunch.getLunchType(), is(LunchType.LUNCH));
        assertThat(lunch.getDishes(), equalTo(DISHES));
        verify(dishMapper).mapEntityToDomain(DISH_ENTITY);
    }

    @Test
    public void mapEntityToDomainShouldReturnNull() {
        final Lunch lunch = lunchMapper.mapEntityToDomain(null);
        assertNull(lunch);
    }

    @Test
    public void mapDomainToEntity() {
        when(dishMapper.mapDomainToEntity(DISH)).thenReturn(DISH_ENTITY);

        final LunchEntity lunchEntity = lunchMapper.mapDomainToEntity(LUNCH);

        assertThat(lunchEntity.getId(), is(LUNCH.getId()));
        assertThat(lunchEntity.getName(), is(LUNCH.getName()));
        assertThat(lunchEntity.getDescription(), is(LUNCH.getDescription()));
        assertThat(lunchEntity.getImg(), is(LUNCH.getImg()));
        assertThat(lunchEntity.getLunchTypeEntity(), is(LunchTypeEntity.LUNCH));
        assertThat(lunchEntity.getDishEntities(), equalTo(DISH_ENTITIES));
        verify(dishMapper).mapDomainToEntity(DISH);
    }

    @Test
    public void mapDomainToEntityShouldReturnNull() {
        final LunchEntity lunchEntity = lunchMapper.mapDomainToEntity(null);
        assertNull(lunchEntity);
    }

    private Lunch initLunch() {
        final Lunch lunch = new Lunch();
        lunch.setId(1L);
        lunch.setName("Lunch");
        lunch.setDescription("Description");
        lunch.setImg("Image");
        lunch.setLunchType(LunchType.LUNCH);
        lunch.setDishes(DISHES);
        return lunch;
    }

    private LunchEntity initLunchEntity() {
        final LunchEntity lunchEntity = new LunchEntity();
        lunchEntity.setId(1L);
        lunchEntity.setName("Lunch");
        lunchEntity.setDescription("Description");
        lunchEntity.setImg("Image");
        lunchEntity.setLunchTypeEntity(LunchTypeEntity.LUNCH);
        lunchEntity.setDishEntities(DISH_ENTITIES);
        return lunchEntity;
    }
}