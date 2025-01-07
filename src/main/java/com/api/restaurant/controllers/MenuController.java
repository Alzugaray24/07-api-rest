package com.api.restaurant.controllers;

import com.api.restaurant.dto.menu.MenuRequestDTO;
import com.api.restaurant.dto.menu.MenuResponseDTO;
import com.api.restaurant.dto.dish.DishResponseDTO;
import com.api.restaurant.models.Menu;
import com.api.restaurant.models.Dish;
import com.api.restaurant.services.MenuService;
import com.api.restaurant.services.DishService;
import com.api.restaurant.services.menu.*;
import com.api.restaurant.services.interfaces.ICommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Menu menu = new Menu();
        menu.setName(menuRequest.getName());
        List<Dish> dishes = menuRequest.getDishIds().stream()
                .map(dishService::getDishById)
                .collect(Collectors.toList());
        menu.setDishes(dishes);
        ICommand<Menu> command = new SaveMenuCommand(menuService.getMenuRepository(), menu);
        Menu savedMenu = command.execute();
        MenuResponseDTO response = convertToMenuResponseDTO(savedMenu);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuResponseDTO> getMenuById(@PathVariable Long id) {
        ICommand<Menu> command = new GetMenuByIdCommand(menuService.getMenuRepository(), id);
        Menu menu = command.execute();
        if (menu == null) {
            return ResponseEntity.notFound().build();
        }
        MenuResponseDTO response = convertToMenuResponseDTO(menu);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuResponseDTO> updateMenu(@PathVariable Long id, @RequestBody MenuRequestDTO menuRequest) {
        Menu updatedMenu = new Menu();
        updatedMenu.setName(menuRequest.getName());
        List<Dish> dishes = menuRequest.getDishIds().stream()
                .map(dishService::getDishById)
                .collect(Collectors.toList());
        updatedMenu.setDishes(dishes);
        ICommand<Menu> command = new UpdateMenuCommand(menuService.getMenuRepository(), id, updatedMenu);
        command.execute();
        MenuResponseDTO response = convertToMenuResponseDTO(updatedMenu);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        ICommand<Void> command = new DeleteMenuCommand(menuService.getMenuRepository(), id);
        command.execute();
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{menuId}/dishes/{dishId}")
    public ResponseEntity<MenuResponseDTO> addDishToMenu(@PathVariable Long menuId, @PathVariable Long dishId) {
        Dish dish = dishService.getDishById(dishId);
        if (dish == null) {
            return ResponseEntity.notFound().build();
        }
        ICommand<Menu> command = new AddDishToMenuCommand(menuService.getMenuRepository(), dishService.getDishRepository(), menuId, dish);
        Menu updatedMenu = command.execute();
        MenuResponseDTO response = convertToMenuResponseDTO(updatedMenu);
        return ResponseEntity.ok(response);
    }

    private MenuResponseDTO convertToMenuResponseDTO(Menu menu) {
        MenuResponseDTO response = new MenuResponseDTO();
        response.setId(menu.getId());
        response.setName(menu.getName());
        response.setDishes(menu.getDishes().stream()
                .map(DishResponseDTO::new)
                .collect(Collectors.toList()));
        return response;
    }
}