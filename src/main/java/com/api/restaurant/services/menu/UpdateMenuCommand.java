package com.api.restaurant.services.menu;

import com.api.restaurant.models.Menu;
import com.api.restaurant.repositories.MenuRepository;
import com.api.restaurant.services.interfaces.ICommand;

public class UpdateMenuCommand implements ICommand<Menu> {
    private final MenuRepository menuRepository;
    private final Long id;
    private final Menu updatedMenu;

    public UpdateMenuCommand(MenuRepository menuRepository, Long id, Menu updatedMenu) {
        this.menuRepository = menuRepository;
        this.id = id;
        this.updatedMenu = updatedMenu;
    }

    @Override
    public Menu execute() {
        return menuRepository.findById(id).map(menu -> {
            menu.setName(updatedMenu.getName());
            menu.setDishes(updatedMenu.getDishes());
            return menuRepository.save(menu);
        }).orElseThrow(() -> new RuntimeException("The menu with id " + id + " was not found"));
    }
}