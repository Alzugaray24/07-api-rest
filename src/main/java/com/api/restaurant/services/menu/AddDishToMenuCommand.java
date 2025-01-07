package com.api.restaurant.services.menu;

import com.api.restaurant.models.Dish;
import com.api.restaurant.models.Menu;
import com.api.restaurant.repositories.MenuRepository;
import com.api.restaurant.services.interfaces.ICommand;

public class AddDishToMenuCommand implements ICommand<Menu> {
    private final MenuRepository menuRepository;
    private final Long menuId;
    private final Dish dish;

    public AddDishToMenuCommand(MenuRepository menuRepository, Long menuId, Dish dish) {
        this.menuRepository = menuRepository;
        this.menuId = menuId;
        this.dish = dish;
    }

    @Override
    public Menu execute() {
        return menuRepository.findById(menuId).map(menu -> {
            menu.getDishes().add(dish);
            return menuRepository.save(menu);
        }).orElseThrow(() -> new RuntimeException("El menú con el id " + menuId + " no se ha encontrado"));
    }
}