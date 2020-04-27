package com.spring.restaurant.service.mapper;

import com.spring.restaurant.domain.Dish;
import com.spring.restaurant.domain.DishType;
import com.spring.restaurant.entity.DishEntity;
import com.spring.restaurant.entity.DishTypeEntity;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DishMapper implements Mapper<Dish, DishEntity> {
    private final IngredientMapper ingredientMapper;

    @Override
    public Dish mapEntityToDomain(DishEntity entity) {
        if (entity == null) {
            return null;
        }
        Dish dish = new Dish();
        dish.setId(entity.getId());
        dish.setName(entity.getName());
        dish.setAbout(entity.getAbout());
        dish.setDishType(DishType.valueOf(entity.getDishTypeEntity().name()));
        dish.setPrice(entity.getPrice());
        dish.setWeight(entity.getWeight());
        dish.setImg(entity.getImg());
        dish.setIngredients(entity.getIngredientEntities().stream()
                .map(ingredientMapper::mapEntityToDomain).collect(Collectors.toList()));
        return dish;
    }

    @Override
    public DishEntity mapDomainToEntity(Dish domain) {
        if (domain == null) {
            return null;
        }
        DishEntity dishEntity = new DishEntity();
        dishEntity.setId(domain.getId());
        dishEntity.setAbout(domain.getAbout());
        dishEntity.setName(domain.getName());
        dishEntity.setDishTypeEntity(DishTypeEntity.valueOf(domain.getDishType().name()));
        dishEntity.setPrice(domain.getPrice());
        dishEntity.setWeight(domain.getWeight());
        dishEntity.setImg(domain.getImg());
        dishEntity.setIngredientEntities(domain.getIngredients().stream()
                .map(ingredientMapper::mapDomainToEntity).collect(Collectors.toList()));
        return dishEntity;
    }
}
