package com.api.restaurant.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "menu", fetch = FetchType.EAGER)
    private List<Dish> dishes;

    public Menu() {
    }

    public Menu(String name) {
        this.name = name;
    }
}