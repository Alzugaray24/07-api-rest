package com.api.restaurant.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private double price;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    public Dish() {
    }

    public Dish(String name, double price) {
        this.name = name;
        this.price = price;
    }
}