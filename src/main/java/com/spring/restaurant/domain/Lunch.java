package com.spring.restaurant.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Lunch {
    private Long id;
    private String name;
    private String description;
    private String img;
    private LunchType lunchType;
    private List<Dish> dishes;

    public LocalTime getTimeFrom() {
        return lunchType.getTimeFrom();
    }

    public LocalTime getTimeTo() {
        return lunchType.getTimeTo();
    }

    public int getPrice() {
        int price = 0;
        for (Dish dish : dishes) {
            price += dish.getPriceInt();
        }
        return price;
    }

    public String getWeight() {
        StringBuilder weight = new StringBuilder();
        for (Dish dish : dishes) {
            weight.append(dish.getWeight()).append(" ");
        }
        weight = new StringBuilder(weight.toString().trim());
        weight = new StringBuilder(weight.toString().replace(" ", " / "));
        return weight.toString();
    }
}
