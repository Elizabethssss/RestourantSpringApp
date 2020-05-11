package com.spring.restaurant.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Domain class of Ingredient
 */


@Getter
@Setter
@NoArgsConstructor
public class Ingredient {
    private Long id;
    private String name;
    private String img;
    private List<Dish> dishes;
}
