package com.api.restaurant.services;

import com.api.restaurant.models.Menu;
import com.api.restaurant.models.Dish;
import com.api.restaurant.repositories.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class MenuServiceTest {

    private MenuService menuService;
    private MenuRepository menuRepository;

    @BeforeEach
    void setUp() {
        menuRepository = mock(MenuRepository.class);
        menuService = new MenuService(menuRepository);
    }

    @Test
    @DisplayName("Guardar Menú")
    void testSaveMenu() {
        Menu menu = new Menu();
        menu.setName("Menú 1");

        when(menuRepository.save(any(Menu.class))).thenReturn(menu);

        Menu savedMenu = menuService.saveMenu(menu);

        assertNotNull(savedMenu);
        assertEquals("Menú 1", savedMenu.getName());
        verify(menuRepository).save(menu);
    }

    @Test
    @DisplayName("Obtener Menú por ID")
    void testGetMenuById() {
        Menu menu = new Menu();
        menu.setId(1L);
        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));

        Menu foundMenu = menuService.getMenuById(1L);

        assertNotNull(foundMenu);
        assertEquals(1L, foundMenu.getId());
        verify(menuRepository).findById(1L);
    }

    @Test
    @DisplayName("Actualizar Menú")
    void testUpdateMenu() {
        Menu existingMenu = new Menu();
        existingMenu.setId(1L);
        existingMenu.setName("Menú 1");

        Menu updatedMenu = new Menu();
        updatedMenu.setId(1L);
        updatedMenu.setName("Menú Actualizado");

        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(existingMenu));
        when(menuRepository.save(any(Menu.class))).thenReturn(updatedMenu);

        Menu result = menuService.updateMenu(1L, updatedMenu);

        assertNotNull(result);
        assertEquals("Menú Actualizado", result.getName());
        verify(menuRepository).findById(1L);
        verify(menuRepository).save(existingMenu);
    }

    @Test
    @DisplayName("Eliminar Menú")
    void testDeleteMenu() {
        doNothing().when(menuRepository).deleteById(anyLong());

        menuService.deleteMenu(1L);

        verify(menuRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Agregar Plato al Menú")
    void testAddDishToMenu() {
        Menu menu = new Menu();
        menu.setId(1L);
        Dish dish = new Dish();
        dish.setId(1L);

        when(menuRepository.findById(anyLong())).thenReturn(Optional.of(menu));
        when(menuRepository.save(any(Menu.class))).thenReturn(menu);

        Menu result = menuService.addDishToMenu(1L, dish);

        assertNotNull(result);
        assertTrue(result.getDishes().contains(dish));
        verify(menuRepository).findById(1L);
        verify(menuRepository).save(menu);
    }

    @Test
    @DisplayName("Obtener Todos los Menús")
    void testGetAllMenus() {
        Menu menu1 = new Menu();
        menu1.setId(1L);
        Menu menu2 = new Menu();
        menu2.setId(2L);

        when(menuRepository.findAll()).thenReturn(List.of(menu1, menu2));

        List<Menu> menus = menuService.getAllMenus();

        assertNotNull(menus);
        assertEquals(2, menus.size());
        verify(menuRepository).findAll();
    }
}