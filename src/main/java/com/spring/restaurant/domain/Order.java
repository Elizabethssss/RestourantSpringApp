package com.spring.restaurant.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Domain class of Order
 */

@Getter
@Setter
@NoArgsConstructor
public class Order {
    private Long id;
    private OrderStatus status;
    private int cost;
    private LocalDate createdAt;
    private User user;
    private List<Dish> dishes;
    private List<Lunch> lunches;

    /**
     * Use to get Dish - Integer Map from Dish List (dishes)
     *
     * @return Dish - Integer Map of Dish List
     */

    public Map<Dish, Integer> getDishIntegerMap() {
        Map<Dish, Integer> dishIntegerMap = new LinkedHashMap<>();
        for(Dish dish : dishes) {
            if(dishIntegerMap.isEmpty() || dishIntegerMap.keySet().stream()
                    .noneMatch(d -> d.getId().equals(dish.getId()))) {
                dishIntegerMap.put(dish, 1);
            }
            else {
                for (Map.Entry<Dish, Integer> entry : dishIntegerMap.entrySet()) {
                    if (entry.getKey().getId().equals(dish.getId())) {
                        dishIntegerMap.replace(entry.getKey(), entry.getValue() + 1);
                    }
                }
            }
        }
        return dishIntegerMap;
    }

    /**
     * Use to get Lunch - Integer Map from Lunch List (lunches)
     *
     * @return Lunch - Integer Map of Lunch List
     */

    public Map<Lunch, Integer> getLunchIntegerMap() {
        Map<Lunch, Integer> lunchIntegerMap = new LinkedHashMap<>();
        for(Lunch lunch : lunches) {
            if(lunchIntegerMap.isEmpty() || lunchIntegerMap.keySet().stream()
                    .noneMatch(l -> l.getId().equals(lunch.getId()))) {
                lunchIntegerMap.put(lunch, 1);
            }
            else {
                for (Map.Entry<Lunch, Integer> entry : lunchIntegerMap.entrySet()) {
                    if (entry.getKey().getId().equals(lunch.getId())) {
                        lunchIntegerMap.replace(entry.getKey(), entry.getValue() + 1);
                    }
                }
            }
        }
        return lunchIntegerMap;
    }

}
