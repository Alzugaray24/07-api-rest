package com.api.restaurant.services.menu;

import com.api.restaurant.models.Menu;
import com.api.restaurant.repositories.MenuRepository;
import com.api.restaurant.services.interfaces.ICommand;

public class GetMenuByIdCommand implements ICommand<Menu> {
    private final MenuRepository menuRepository;
    private final Long id;

    public GetMenuByIdCommand(MenuRepository menuRepository, Long id) {
        this.menuRepository = menuRepository;
        this.id = id;
    }

    @Override
    public Menu execute() {
        return menuRepository.findById(id).orElse(null);
    }
}