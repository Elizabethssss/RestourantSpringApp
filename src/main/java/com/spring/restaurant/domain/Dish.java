package com.spring.restaurant.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    public int getPriceInt() {
        return (int) price;
    }
}
