package com.spring.restaurant.service.mapper;

import com.spring.restaurant.domain.Dish;
import com.spring.restaurant.domain.DishType;
import com.spring.restaurant.domain.Ingredient;
import com.spring.restaurant.domain.Lunch;
import com.spring.restaurant.entity.DishEntity;
import com.spring.restaurant.entity.DishTypeEntity;
import com.spring.restaurant.entity.IngredientEntity;
import com.spring.restaurant.entity.LunchEntity;
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
public class DishMapperTest {
    private static Dish DISH;
    private static DishEntity DISH_ENTITY ;
    private static final Ingredient INGREDIENT = new Ingredient();
    private static final IngredientEntity INGREDIENT_ENTITY = new IngredientEntity();
    private static List<Ingredient> INGREDIENTS;
    private static List<IngredientEntity> INGREDIENT_ENTITIES;

    @Mock
    private IngredientMapper ingredientMapper;
    @InjectMocks
    private DishMapper dishMapper;

    @Before
    public void setUp() {
        INGREDIENTS = new ArrayList<>();
        INGREDIENTS.add(INGREDIENT);
        INGREDIENT_ENTITIES = new ArrayList<>();
        INGREDIENT_ENTITIES.add(INGREDIENT_ENTITY);
        DISH = initDish();
        DISH_ENTITY = initDishEntity();
    }

    @Test
    public void mapEntityToDomain() {
        when(ingredientMapper.mapEntityToDomain(INGREDIENT_ENTITY)).thenReturn(INGREDIENT);

        final Dish dish = dishMapper.mapEntityToDomain(DISH_ENTITY);

        assertThat(dish.getId(), is(DISH_ENTITY.getId()));
        assertThat(dish.getName(), is(DISH_ENTITY.getName()));
        assertThat(dish.getAbout(), is(DISH_ENTITY.getAbout()));
        assertThat(dish.getDishType(), is(DishType.DESSERT));
        assertThat(dish.getPrice(), is(DISH_ENTITY.getPrice()));
        assertThat(dish.getWeight(), is(DISH_ENTITY.getWeight()));
        assertThat(dish.getImg(), is(DISH_ENTITY.getImg()));
        assertThat(dish.getIngredients(), equalTo(INGREDIENTS));
        verify(ingredientMapper).mapEntityToDomain(INGREDIENT_ENTITY);
    }

    @Test
    public void mapEntityToDomainShouldReturnNull() {
        final Dish dish = dishMapper.mapEntityToDomain(null);
        assertNull(dish);
    }

    @Test
    public void mapDomainToEntity() {
        when(ingredientMapper.mapDomainToEntity(INGREDIENT)).thenReturn(INGREDIENT_ENTITY);

        final DishEntity dishEntity = dishMapper.mapDomainToEntity(DISH);

        assertThat(dishEntity.getId(), is(DISH.getId()));
        assertThat(dishEntity.getName(), is(DISH.getName()));
        assertThat(dishEntity.getAbout(), is(DISH.getAbout()));
        assertThat(dishEntity.getDishTypeEntity(), is(DishTypeEntity.DESSERT));
        assertThat(dishEntity.getPrice(), is(DISH.getPrice()));
        assertThat(dishEntity.getWeight(), is(DISH.getWeight()));
        assertThat(dishEntity.getImg(), is(DISH.getImg()));
        assertThat(dishEntity.getIngredientEntities(), equalTo(INGREDIENT_ENTITIES));
        verify(ingredientMapper).mapDomainToEntity(INGREDIENT);
    }

    @Test
    public void mapDomainToEntityShouldReturnNull() {
        final DishEntity dishEntity = dishMapper.mapDomainToEntity(null);
        assertNull(dishEntity);
    }

    private Dish initDish() {
        final Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Dish name");
        dish.setAbout("About dish");
        dish.setDishType(DishType.DESSERT);
        dish.setPrice(100);
        dish.setWeight(100);
        dish.setImg("Image");
        dish.setIngredients(INGREDIENTS);
        return dish;
    }

    private DishEntity initDishEntity() {
        final DishEntity dishEntity = new DishEntity();
        dishEntity.setId(1L);
        dishEntity.setName("Dish name");
        dishEntity.setAbout("About dish");
        dishEntity.setDishTypeEntity(DishTypeEntity.DESSERT);
        dishEntity.setPrice(100);
        dishEntity.setWeight(100);
        dishEntity.setImg("Image");
        dishEntity.setIngredientEntities(INGREDIENT_ENTITIES);
        return dishEntity;
    }
}