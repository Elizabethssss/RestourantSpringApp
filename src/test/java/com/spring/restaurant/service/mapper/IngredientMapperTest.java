package com.spring.restaurant.service.mapper;

import com.spring.restaurant.domain.Ingredient;
import com.spring.restaurant.domain.User;
import com.spring.restaurant.entity.IngredientEntity;
import com.spring.restaurant.entity.UserEntity;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class IngredientMapperTest {
    public static final Ingredient INGREDIENT = initIngredient();

    public static final IngredientEntity INGREDIENT_ENTITY = initIngredientEntity();

    private Mapper<Ingredient, IngredientEntity> mapper = new IngredientMapper();


    @Test
    public void mapEntityToDomain() {
        final Ingredient ingredient = mapper.mapEntityToDomain(INGREDIENT_ENTITY);

        assertThat(ingredient.getId(), is(INGREDIENT_ENTITY.getId()));
        assertThat(ingredient.getName(), is(INGREDIENT_ENTITY.getName()));
        assertThat(ingredient.getImg(), is(INGREDIENT_ENTITY.getImg()));
    }

    @Test
    public void mapEntityToDomainShouldReturnNull() {
        final Ingredient ingredient = mapper.mapEntityToDomain(null);
        assertNull(ingredient);
    }

    @Test
    public void mapDomainToEntity() {
        final IngredientEntity ingredientEntity = mapper.mapDomainToEntity(INGREDIENT);

        assertThat(ingredientEntity.getId(), is(INGREDIENT.getId()));
        assertThat(ingredientEntity.getName(), is(INGREDIENT.getName()));
        assertThat(ingredientEntity.getImg(), is(INGREDIENT.getImg()));
    }

    @Test
    public void mapDomainToEntityShouldReturnNull() {
        final IngredientEntity ingredientEntity = mapper.mapDomainToEntity(null);
        assertNull(ingredientEntity);
    }

    private static Ingredient initIngredient() {
        final Ingredient ingredient = new Ingredient();
        ingredient.setId(1L);
        ingredient.setName("Potato");
        ingredient.setImg("potato.jpg");
        return ingredient;
    }

    private static IngredientEntity initIngredientEntity() {
        final IngredientEntity ingredientEntity = new IngredientEntity();
        ingredientEntity.setId(1L);
        ingredientEntity.setName("Potato");
        ingredientEntity.setImg("potato.jpg");
        return ingredientEntity;
    }
}