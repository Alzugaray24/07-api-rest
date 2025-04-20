package com.api.restaurant.controllers;

import com.api.restaurant.dto.menu.MenuRequestDTO;
import com.api.restaurant.dto.menu.MenuResponseDTO;
import com.api.restaurant.dto.menuitem.AddMenuItemRequestDTO;
import com.api.restaurant.models.Menu;
import com.api.restaurant.models.Dish;
import com.api.restaurant.models.MenuItem;
import com.api.restaurant.services.MenuService;
import com.api.restaurant.services.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final MenuService menuService;
    private final DishService dishService;

    @Autowired
    public MenuController(MenuService menuService, DishService dishService) {
        this.menuService = menuService;
        this.dishService = dishService;
    }

    @PostMapping
    public ResponseEntity<MenuResponseDTO> saveMenu(@RequestBody MenuRequestDTO menuRequest) {
        // Validar que el nombre no sea nulo o vacío
        if (menuRequest.getName() == null || menuRequest.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Create a new menu
        Menu menu = new Menu();
        menu.setName(menuRequest.getName());
        menu.setActive(menuRequest.isActive());

        // Process menu items
        List<MenuItem> menuItems = new ArrayList<>();
        if (menuRequest.getItems() != null && !menuRequest.getItems().isEmpty()) {
            for (var itemRequest : menuRequest.getItems()) {
                // Validar que el dishId no sea nulo
                if (itemRequest.getDishId() == null) {
                    continue;
                }

                Dish dish = dishService.getDishById(itemRequest.getDishId());
                if (dish == null) {
                    continue;
                }

                MenuItem menuItem = new MenuItem();
                menuItem.setDish(dish);
                menuItem.setCategory(
                        itemRequest.getCategory() != null ? itemRequest.getCategory() : determineDefaultCategory());
                menuItem.setSpecialPrice(itemRequest.getSpecialPrice());
                menuItem.setAvailable(itemRequest.isAvailable());
                menuItem.setMenu(menu);
                menuItems.add(menuItem);
            }
        }

        menu.setMenuItems(menuItems);

        // Save the menu
        Menu savedMenu = menuService.saveMenu(menu);
        MenuResponseDTO response = new MenuResponseDTO(savedMenu);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuResponseDTO> getMenuById(@PathVariable Long id) {
        Menu menu = menuService.getMenuById(id);
        if (menu == null) {
            return ResponseEntity.notFound().build();
        }
        MenuResponseDTO response = new MenuResponseDTO(menu);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<MenuResponseDTO>> getAllMenus() {
        List<Menu> menus = menuService.getAllMenus();
        List<MenuResponseDTO> responses = menus.stream()
                .map(MenuResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuResponseDTO> updateMenu(@PathVariable Long id, @RequestBody MenuRequestDTO menuRequest) {
        // Validar que el nombre no sea nulo o vacío
        if (menuRequest.getName() == null || menuRequest.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // Check if menu exists
        Menu existingMenu = menuService.getMenuById(id);
        if (existingMenu == null) {
            return ResponseEntity.notFound().build();
        }

        // Create updated menu
        Menu updatedMenu = new Menu();
        updatedMenu.setName(menuRequest.getName());
        updatedMenu.setActive(menuRequest.isActive());

        // Process menu items
        List<MenuItem> menuItems = new ArrayList<>();
        if (menuRequest.getItems() != null && !menuRequest.getItems().isEmpty()) {
            for (var itemRequest : menuRequest.getItems()) {
                // Validar que el dishId no sea nulo
                if (itemRequest.getDishId() == null) {
                    continue;
                }

                Dish dish = dishService.getDishById(itemRequest.getDishId());
                if (dish == null) {
                    continue;
                }

                MenuItem menuItem = new MenuItem();
                menuItem.setDish(dish);
                menuItem.setCategory(
                        itemRequest.getCategory() != null ? itemRequest.getCategory() : determineDefaultCategory());
                menuItem.setSpecialPrice(itemRequest.getSpecialPrice());
                menuItem.setAvailable(itemRequest.isAvailable());
                menuItems.add(menuItem);
            }
        }

        updatedMenu.setMenuItems(menuItems);

        // Update the menu
        Menu newMenu = menuService.updateMenu(id, updatedMenu);
        MenuResponseDTO response = new MenuResponseDTO(newMenu);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        try {
            Menu menu = menuService.getMenuById(id);
            if (menu == null) {
                return ResponseEntity.notFound().build();
            }

            menuService.deleteMenu(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{menuId}/items")
    public ResponseEntity<MenuResponseDTO> addDishToMenu(
            @PathVariable Long menuId,
            @RequestBody AddMenuItemRequestDTO itemRequest) {

        // Validar el menuId y dishId
        if (itemRequest.getDishId() == null) {
            return ResponseEntity.badRequest().build();
        }

        // Obtener el menú
        Menu menu = menuService.getMenuById(menuId);
        if (menu == null) {
            return ResponseEntity.notFound().build();
        }

        // Obtener el plato
        Dish dish = dishService.getDishById(itemRequest.getDishId());
        if (dish == null) {
            return ResponseEntity.notFound().build();
        }

        // Crear el nuevo MenuItem
        MenuItem menuItem = new MenuItem();
        menuItem.setDish(dish);
        menuItem.setCategory(
                itemRequest.getCategory() != null ? itemRequest.getCategory() : determineDefaultCategory());
        menuItem.setSpecialPrice(itemRequest.getSpecialPrice());
        menuItem.setAvailable(itemRequest.isAvailable());

        // Agregar el MenuItem al menú
        menu.addMenuItem(menuItem);

        // Guardar el menú actualizado
        Menu updatedMenu = menuService.saveMenu(menu);

        // Devolver el menú actualizado
        MenuResponseDTO response = new MenuResponseDTO(updatedMenu);
        return ResponseEntity.ok(response);
    }

    // Método auxiliar para determinar la categoría por defecto
    private com.api.restaurant.models.MenuItemCategory determineDefaultCategory() {
        return com.api.restaurant.models.MenuItemCategory.MAIN_COURSE;
    }
}