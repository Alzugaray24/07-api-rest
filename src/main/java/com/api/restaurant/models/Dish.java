package com.api.restaurant.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL)
    private List<MenuItem> menuItems = new ArrayList<>();

    @OneToMany(mappedBy = "dish")
    private List<OrderItem> orderItems = new ArrayList<>();

    public Dish() {
    }

    public Dish(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Dish(String name, double price, DishEnum type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }
}