package com.api.restaurant.services.menu;

import com.api.restaurant.models.Dish;
import com.api.restaurant.models.Menu;
import com.api.restaurant.repositories.DishRepository;
import com.api.restaurant.repositories.MenuRepository;
import com.api.restaurant.services.interfaces.ICommand;

public class AddDishToMenuCommand implements ICommand<Menu> {
    private final MenuRepository menuRepository;
    private final DishRepository dishRepository;
    private final Long menuId;
    private final Dish dish;

    public AddDishToMenuCommand(MenuRepository menuRepository, DishRepository dishRepository, Long menuId, Dish dish) {
        this.menuRepository = menuRepository;
        this.dishRepository = dishRepository;
        this.menuId = menuId;
        this.dish = dish;
    }

    @Override
    public Menu execute() {
        Dish savedDish = dishRepository.save(dish);

        System.out.println("savedDish = " + savedDish.getName());

        return menuRepository.findById(menuId).map(menu -> {
            menu.getDishes().add(savedDish);
            System.out.println("menu = " + menu.getDishes());
            return menuRepository.save(menu);
        }).orElseThrow(() -> new RuntimeException("El menú con el id " + menuId + " no se ha encontrado"));
    }
}