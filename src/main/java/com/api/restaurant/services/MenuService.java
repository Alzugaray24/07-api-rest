package com.api.restaurant.services;

import com.api.restaurant.models.Menu;
import com.api.restaurant.repositories.MenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    public Menu getMenuById(Long id) {
        return menuRepository.findById(id).orElse(null);
    }

    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }

    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

    public void updateMenu(Long id, Menu updatedMenu) {
        menuRepository.findById(id).map(menu -> {
            menu.setName(updatedMenu.getName());
            menu.setDishes(updatedMenu.getDishes());
            return menuRepository.save(menu);
        }).orElseThrow(() -> new RuntimeException("The menu with id " + id + " was not found"));
    }
}