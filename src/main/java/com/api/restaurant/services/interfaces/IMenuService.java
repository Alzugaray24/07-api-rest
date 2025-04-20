package com.api.restaurant.services.interfaces;

import com.api.restaurant.models.Dish;
import com.api.restaurant.models.Menu;
import com.api.restaurant.models.MenuItem;
import com.api.restaurant.models.MenuItemCategory;

import java.util.List;

public interface IMenuService {
    Menu saveMenu(Menu menu);

    Menu getMenuById(Long id);

    List<Menu> getAllMenus();

    void deleteMenu(Long id);

    Menu updateMenu(Long id, Menu updatedMenu);

    /**
     * Agrega un item al menú especificado
     * 
     * @param menuId       ID del menú
     * @param dish         Plato a agregar
     * @param category     Categoría del item de menú
     * @param specialPrice Precio especial (opcional)
     * @param available    Disponibilidad
     * @return El menú actualizado
     */
    Menu addItemToMenu(Long menuId, Dish dish, MenuItemCategory category, Double specialPrice, boolean available);
}