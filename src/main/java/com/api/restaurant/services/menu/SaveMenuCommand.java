package com.api.restaurant.services.menu;

import com.api.restaurant.models.Menu;
import com.api.restaurant.repositories.MenuRepository;
import com.api.restaurant.services.interfaces.ICommand;

public class SaveMenuCommand implements ICommand<Menu> {
    private final MenuRepository menuRepository;
    private final Menu menu;

    public SaveMenuCommand(MenuRepository menuRepository, Menu menu) {
        this.menuRepository = menuRepository;
        this.menu = menu;
    }

    @Override
    public Menu execute() {
        return menuRepository.save(menu);
    }
}