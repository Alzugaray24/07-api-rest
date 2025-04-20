package com.api.restaurant.services.interfaces;

import com.api.restaurant.models.Menu;

import java.util.List;

public interface IMenuService {
    Menu saveMenu(Menu menu);

    Menu getMenuById(Long id);

    List<Menu> getAllMenus();

    void deleteMenu(Long id);

    Menu updateMenu(Long id, Menu updatedMenu);
}