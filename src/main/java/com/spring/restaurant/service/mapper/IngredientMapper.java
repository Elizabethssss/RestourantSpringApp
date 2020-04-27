package com.spring.restaurant.service.mapper;

import com.spring.restaurant.domain.Ingredient;
import com.spring.restaurant.entity.IngredientEntity;
import org.springframework.stereotype.Component;

@Component
public class IngredientMapper implements Mapper<Ingredient, IngredientEntity> {
    @Override
    public Ingredient mapEntityToDomain(IngredientEntity entity) {
        if(entity == null) {
            return null;
        }
        Ingredient ingredient = new Ingredient();
        ingredient.setId(entity.getId());
        ingredient.setName(entity.getName());
        ingredient.setImg(entity.getImg());
        return ingredient;
    }

    @Override
    public IngredientEntity mapDomainToEntity(Ingredient domain) {
        if(domain == null) {
            return null;
        }
        IngredientEntity ingredientEntity = new IngredientEntity();
        ingredientEntity.setId(domain.getId());
        ingredientEntity.setName(domain.getName());
        ingredientEntity.setImg(domain.getImg());
        return ingredientEntity;
    }
}
