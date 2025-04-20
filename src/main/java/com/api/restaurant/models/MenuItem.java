package com.api.restaurant.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "menu_item")
public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id")
    private Dish dish;

    private Double specialPrice;

    private boolean available = true;

    @Enumerated(EnumType.STRING)
    private MenuItemCategory category;

    public MenuItem() {
    }

    public MenuItem(Dish dish, MenuItemCategory category) {
        this.dish = dish;
        this.category = category;
        this.specialPrice = dish.getPrice(); // Por defecto mismo precio que el plato
    }

    public MenuItem(Dish dish, MenuItemCategory category, Double specialPrice) {
        this(dish, category);
        this.specialPrice = specialPrice;
    }

    // Método para obtener el precio final (especial o del plato)
    public Double getFinalPrice() {
        return specialPrice != null ? specialPrice : dish.getPrice();
    }
}