package com.spring.restaurant.service.mapper;

import com.spring.restaurant.domain.Lunch;
import com.spring.restaurant.domain.LunchType;
import com.spring.restaurant.entity.LunchEntity;
import com.spring.restaurant.entity.LunchTypeEntity;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class LunchMapper implements Mapper<Lunch, LunchEntity> {
    private final DishMapper dishMapper;

    @Override
    public Lunch mapEntityToDomain(LunchEntity entity) {
        if (entity == null) {
            return null;
        }
        Lunch lunch = new Lunch();
        lunch.setId(entity.getId());
        lunch.setName(entity.getName());
        lunch.setDescription(entity.getDescription());
        lunch.setImg(entity.getImg());
        lunch.setLunchType(LunchType.valueOf(entity.getLunchTypeEntity().name()));
        lunch.setDishes(entity.getDishEntities().stream()
                .map(dishMapper::mapEntityToDomain).collect(Collectors.toList()));
        return lunch;
    }

    @Override
    public LunchEntity mapDomainToEntity(Lunch domain) {
        if (domain == null) {
            return null;
        }
        LunchEntity lunchEntity = new LunchEntity();
        lunchEntity.setId(domain.getId());
        lunchEntity.setName(domain.getName());
        lunchEntity.setDescription(domain.getDescription());
        lunchEntity.setImg(domain.getImg());
        lunchEntity.setLunchTypeEntity(LunchTypeEntity.valueOf(domain.getLunchType().name()));
        lunchEntity.setDishEntities(domain.getDishes().stream()
                .map(dishMapper::mapDomainToEntity).collect(Collectors.toList()));
        return lunchEntity;
    }
}
