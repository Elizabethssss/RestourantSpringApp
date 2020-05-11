package com.spring.restaurant.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Entity class of Dish
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "dishes")
public class DishEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "about", nullable = false)
    private String about;

    @Enumerated(EnumType.STRING)
    @Column(name = "dish_type", nullable = false)
    private DishTypeEntity dishTypeEntity;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "weight", nullable = false)
    private int weight;

    @Column(name = "img", nullable = false)
    private String img;


    @ManyToMany(mappedBy = "dishEntities")
    private List<OrderEntity> orderEntities;

    @ManyToMany(mappedBy = "dishEntities")
    private List<LunchEntity> lunchEntities;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "dishes_ingredients",
        joinColumns = @JoinColumn(name = "dish_id"),
        inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private List<IngredientEntity> ingredientEntities;

}
