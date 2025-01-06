package com.api.restaurant.services.menu;

import com.api.restaurant.repositories.MenuRepository;
import com.api.restaurant.services.interfaces.ICommand;


public class DeleteMenuCommand implements ICommand<Void> {
    private final MenuRepository menuRepository;
    private final Long id;

    public DeleteMenuCommand(MenuRepository menuRepository, Long id) {
        this.menuRepository = menuRepository;
        this.id = id;
    }

    @Override
    public Void execute() {
        menuRepository.deleteById(id);
        return null;
    }
}