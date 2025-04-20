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
        try {
            // Crear el menú a partir del DTO
            Menu menu = new Menu();
            menu.setName(menuRequest.getName());
            menu.setActive(true);

            // Crear la lista de ítems del menú
            if (menuRequest.getItems() != null && !menuRequest.getItems().isEmpty()) {
                for (com.api.restaurant.dto.menuitem.MenuItemRequestDTO itemDTO : menuRequest.getItems()) {
                    if (itemDTO.getDishId() != null) {
                        // Buscar el plato por ID
                        Dish dish = dishService.getDishById(itemDTO.getDishId());
                        if (dish != null) {
                            // Crear el MenuItem y asignar valores
                            MenuItem menuItem = new MenuItem();
                            menuItem.setDish(dish);
                            menuItem.setCategory(itemDTO.getCategory());
                            menuItem.setSpecialPrice(itemDTO.getSpecialPrice());
                            menuItem.setAvailable(itemDTO.isAvailable());

                            // Agregar al menú
                            menu.addMenuItem(menuItem);
                        }
                    }
                }
            }

            // Guardar el menú
            Menu savedMenu = menuService.saveMenu(menu);

            // Devolver la respuesta
            MenuResponseDTO response = new MenuResponseDTO(savedMenu);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuResponseDTO> getMenuById(@PathVariable Long id) {
        Menu menu = menuService.getMenuById(id);
        if (menu == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new MenuResponseDTO(menu));
    }

    @GetMapping
    public ResponseEntity<List<MenuResponseDTO>> getAllMenus() {
        List<Menu> menus = menuService.getAllMenus();
        List<MenuResponseDTO> menuDTOs = menus.stream()
                .map(MenuResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(menuDTOs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuResponseDTO> updateMenu(@PathVariable Long id, @RequestBody MenuRequestDTO menuRequest) {
        try {
            // Obtener el menú existente
            Menu existingMenu = menuService.getMenuById(id);
            if (existingMenu == null) {
                return ResponseEntity.notFound().build();
            }

            // Actualizar propiedades básicas
            existingMenu.setName(menuRequest.getName());

            // Limpiar los ítems existentes
            existingMenu.getMenuItems().clear();

            // Agregar los nuevos ítems
            if (menuRequest.getItems() != null) {
                for (com.api.restaurant.dto.menuitem.MenuItemRequestDTO itemDTO : menuRequest.getItems()) {
                    if (itemDTO.getDishId() != null) {
                        // Buscar el plato por ID
                        Dish dish = dishService.getDishById(itemDTO.getDishId());
                        if (dish != null) {
                            // Crear el MenuItem y asignar valores
                            MenuItem menuItem = new MenuItem();
                            menuItem.setDish(dish);
                            menuItem.setCategory(itemDTO.getCategory());
                            menuItem.setSpecialPrice(itemDTO.getSpecialPrice());
                            menuItem.setAvailable(itemDTO.isAvailable());

                            // Agregar al menú
                            existingMenu.addMenuItem(menuItem);
                        }
                    }
                }
            }

            // Actualizar el menú
            Menu updatedMenu = menuService.updateMenu(id, existingMenu);

            // Devolver la respuesta
            MenuResponseDTO response = new MenuResponseDTO(updatedMenu);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
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

        // Validar el request
        if (itemRequest.getDishId() == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            // Obtener el plato
            Dish dish = dishService.getDishById(itemRequest.getDishId());
            if (dish == null) {
                return ResponseEntity.notFound().build();
            }

            // Usar el servicio para agregar el item al menú
            Menu updatedMenu = menuService.addItemToMenu(
                    menuId,
                    dish,
                    itemRequest.getCategory(),
                    itemRequest.getSpecialPrice(),
                    itemRequest.isAvailable());

            // Devolver el menú actualizado
            MenuResponseDTO response = new MenuResponseDTO(updatedMenu);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}