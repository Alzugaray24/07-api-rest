package com.api.restaurant.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean active = true;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuItem> menuItems = new ArrayList<>();

    public Menu() {
    }

    public Menu(String name) {
        this.name = name;
    }

    // Helper method to add menu item
    public void addMenuItem(MenuItem menuItem) {
        menuItems.add(menuItem);
        menuItem.setMenu(this);
    }

    // Helper method to remove menu item
    public void removeMenuItem(MenuItem menuItem) {
        menuItems.remove(menuItem);
        menuItem.setMenu(null);
    }

    // Helper method to get dishes by category
    public List<MenuItem> getItemsByCategory(MenuItemCategory category) {
        return menuItems.stream()
                .filter(item -> item.getCategory() == category && item.isAvailable())
                .collect(Collectors.toList());
    }

    // Método de compatibilidad para obtener los platos del menú
    public List<Dish> getDishes() {
        return menuItems.stream()
                .map(MenuItem::getDish)
                .collect(Collectors.toList());
    }

    // Método de compatibilidad para establecer los platos del menú
    public void setDishes(List<Dish> dishes) {
        // Limpiar items existentes
        this.menuItems.clear();

        // Crear MenuItems para cada Dish
        if (dishes != null) {
            for (Dish dish : dishes) {
                MenuItem item = new MenuItem(dish, MenuItemCategory.MAIN_COURSE);
                this.addMenuItem(item);
            }
        }
    }
}