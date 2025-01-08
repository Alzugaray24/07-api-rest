package com.api.restaurant.services;

import com.api.restaurant.models.Menu;
import com.api.restaurant.models.Dish;
import com.api.restaurant.repositories.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    @Autowired
    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public Menu saveMenu(Menu menu) {
        return menuRepository.save(menu);
    }

    public Menu getMenuById(Long id) {
        return menuRepository.findById(id).orElse(null);
    }

    public void updateMenu(Long id, Menu updatedMenu) {
        menuRepository.findById(id).ifPresent(menu -> {
            menu.setName(updatedMenu.getName());
            menu.setDishes(updatedMenu.getDishes());
            menuRepository.save(menu);
        });
    }

    public void deleteMenu(Long id) {
        menuRepository.deleteById(id);
    }

    public Menu addDishToMenu(Long menuId, Dish dish) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new RuntimeException("El menu no se encontro"));
        menu.getDishes().add(dish);
        return menuRepository.save(menu);
    }

    public List<Menu> getAllMenus() {
        return menuRepository.findAll();
    }
}