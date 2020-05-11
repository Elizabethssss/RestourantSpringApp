package com.spring.restaurant.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;

/**
 * Entity class of Lunch
 */

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "lunches")
public class LunchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "img", nullable = false)
    private String img;

    @Enumerated(EnumType.STRING)
    @Column(name = "lunch_type", nullable = false)
    private LunchTypeEntity lunchTypeEntity;

    @ManyToMany
    @JoinTable(name = "dishes_lunches",
            joinColumns = @JoinColumn(name = "lunch_id"),
            inverseJoinColumns = @JoinColumn(name = "dish_id")
    )
    private List<DishEntity> dishEntities;

    @ManyToMany(mappedBy = "lunchEntities")
    private List<OrderEntity> orderEntities;
}
