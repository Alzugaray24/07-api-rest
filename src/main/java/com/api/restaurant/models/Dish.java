package com.api.restaurant.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;

    @Enumerated(EnumType.STRING)
    private DishEnum type = DishEnum.COMMON;

    @ManyToMany(mappedBy = "dishes")
    private Set<Menu> menus = new HashSet<>();

    public Dish() {
    }

    public Dish(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public void setMenu(Menu menu) {
        this.menus.add(menu);
        menu.getDishes().add(this);
    }

}