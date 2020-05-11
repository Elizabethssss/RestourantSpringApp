package com.spring.restaurant.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Domain class of Dish
 */


@Getter
@Setter
@NoArgsConstructor
public class Dish {
    private Long id;
    private String name;
    private String about;
    private DishType dishType;
    private double price;
    private int weight;
    private String img;
    private List<Order> orders;
    private List<Lunch> lunches;
    private List<Ingredient> ingredients;

    /**
     * Use to get integer value of price
     *
     * @return int value of price
     */

    public int getPriceInt() {
        return (int) price;
    }
}
