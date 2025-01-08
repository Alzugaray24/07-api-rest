package com.api.restaurant.controllers;

import com.api.restaurant.dto.menu.MenuRequestDTO;
import com.api.restaurant.dto.menu.MenuResponseDTO;
import com.api.restaurant.models.Menu;
import com.api.restaurant.models.Dish;
import com.api.restaurant.services.MenuService;
import com.api.restaurant.services.DishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class MenuControllerTest {

    private WebTestClient webTestClient;
    private MenuService menuService;
    private DishService dishService;

    @BeforeEach
    void setUp() {
        menuService = mock(MenuService.class);
        dishService = mock(DishService.class);
        webTestClient = WebTestClient.bindToController(new MenuController(menuService, dishService)).build();
    }

    @Test
    @DisplayName("Save Menu")
    void testSaveMenu() {
        MenuRequestDTO requestDTO = new MenuRequestDTO();
        requestDTO.setName("Menu 1");
        requestDTO.setDishIds(List.of(1L, 2L));

        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("Menu 1");
        Dish dish1 = new Dish();
        dish1.setId(1L);
        Dish dish2 = new Dish();
        dish2.setId(2L);
        menu.setDishes(List.of(dish1, dish2));

        when(dishService.getDishById(anyLong())).thenReturn(dish1, dish2);
        when(menuService.saveMenu(any(Menu.class))).thenReturn(menu);

        webTestClient
                .post()
                .uri("/api/menu")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(MenuResponseDTO.class)
                .value(response -> {
                    assertEquals(menu.getId(), response.getId());
                    assertEquals(menu.getName(), response.getName());
                    assertEquals(2, response.getDishes().size());
                });

        verify(menuService).saveMenu(argThat(argument ->
                argument.getName().equals(requestDTO.getName())
        ));
    }

    @Test
    @DisplayName("Get Menu by ID")
    void testGetMenuById() {
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("Menu 1");
        menu.setDishes(List.of());

        when(menuService.getMenuById(anyLong())).thenReturn(menu);

        webTestClient
                .get()
                .uri("/api/menu/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(MenuResponseDTO.class)
                .value(response -> {
                    assertEquals(menu.getId(), response.getId());
                    assertEquals(menu.getName(), response.getName());
                });

        verify(menuService).getMenuById(anyLong());
    }

    @Test
    @DisplayName("Update Menu")
    void testUpdateMenu() {
        MenuRequestDTO requestDTO = new MenuRequestDTO();
        requestDTO.setName("Updated Menu");
        requestDTO.setDishIds(List.of(1L, 2L));

        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("Updated Menu");
        Dish dish1 = new Dish();
        dish1.setId(1L);
        Dish dish2 = new Dish();
        dish2.setId(2L);
        menu.setDishes(List.of(dish1, dish2));

        when(dishService.getDishById(anyLong())).thenReturn(dish1, dish2);
        when(menuService.updateMenu(anyLong(), any(Menu.class))).thenReturn(menu);

        webTestClient
                .put()
                .uri("/api/menu/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(MenuResponseDTO.class)
                .value(response -> {
                    assertEquals(menu.getId(), response.getId());
                    assertEquals(menu.getName(), response.getName());
                });

        verify(menuService).updateMenu(anyLong(), argThat(argument ->
                argument.getName().equals(requestDTO.getName())
        ));
    }

    @Test
    @DisplayName("Delete Menu")
    void testDeleteMenu() {
        doNothing().when(menuService).deleteMenu(anyLong());

        webTestClient
                .delete()
                .uri("/api/menu/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();

        verify(menuService).deleteMenu(anyLong());
    }
}