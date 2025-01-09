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
    @DisplayName("Guardar Menú")
    void testSaveMenu() {
        MenuRequestDTO requestDTO = new MenuRequestDTO();
        requestDTO.setName("Menú 1");
        requestDTO.setDishIds(List.of(1L, 2L));

        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("Menú 1");
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
    @DisplayName("Obtener Menú por ID")
    void testGetMenuById() {
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("Menú 1");
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
    @DisplayName("Obtener Menú por ID - No Encontrado")
    void testGetMenuByIdNotFound() {
        when(menuService.getMenuById(anyLong())).thenReturn(null);

        webTestClient
                .get()
                .uri("/api/menu/{id}", 1L)
                .exchange()
                .expectStatus().isNotFound();

        verify(menuService).getMenuById(anyLong());
    }

    @Test
    @DisplayName("Obtener Todos los Menús")
    void testGetAllMenus() {
        List<Menu> menus = List.of(
                new Menu("Menú 1"),
                new Menu("Menú 2"),
                new Menu("Menú 3")
        );

        when(menuService.getAllMenus()).thenReturn(menus);

        webTestClient
                .get()
                .uri("/api/menu")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(MenuResponseDTO.class)
                .hasSize(3)
                .value(response -> {
                    assertEquals("Menú 1", response.get(0).getName());
                    assertEquals("Menú 2", response.get(1).getName());
                    assertEquals("Menú 3", response.get(2).getName());
                });

        verify(menuService).getAllMenus();
    }

    @Test
    @DisplayName("Actualizar Menú")
    void testUpdateMenu() {
        MenuRequestDTO requestDTO = new MenuRequestDTO();
        requestDTO.setName("Menú Actualizado");
        requestDTO.setDishIds(List.of(1L, 2L));

        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("Menú Actualizado");
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
    @DisplayName("Actualizar Menú - No Encontrado")
    void testUpdateMenuNotFound() {
        MenuRequestDTO requestDTO = new MenuRequestDTO();
        requestDTO.setName("Menú Actualizado");
        requestDTO.setDishIds(List.of(1L, 2L));

        when(menuService.updateMenu(anyLong(), any(Menu.class))).thenReturn(null);

        webTestClient
                .put()
                .uri("/api/menu/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isNotFound();

        verify(menuService).updateMenu(anyLong(), any(Menu.class));
    }

    @Test
    @DisplayName("Eliminar Menú")
    void testDeleteMenu() {
        doNothing().when(menuService).deleteMenu(anyLong());

        webTestClient
                .delete()
                .uri("/api/menu/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();

        verify(menuService).deleteMenu(anyLong());
    }

    @Test
    @DisplayName("Eliminar Menú - No Encontrado")
    void testDeleteMenuNotFound() {
        doThrow(new RuntimeException("Menu not found")).when(menuService).deleteMenu(anyLong());

        webTestClient
                .delete()
                .uri("/api/menu/{id}", 1L)
                .exchange()
                .expectStatus().isNotFound();

        verify(menuService).deleteMenu(anyLong());
    }

    @Test
    @DisplayName("Agregar Plato a Menú")
    void testAddDishToMenu() {
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("Menú 1");
        Dish dish = new Dish();
        dish.setId(1L);
        menu.setDishes(List.of());

        when(dishService.getDishById(anyLong())).thenReturn(dish);
        when(menuService.getMenuById(anyLong())).thenReturn(menu);
        when(menuService.saveMenu(any(Menu.class))).thenReturn(menu);
        when(dishService.saveDish(any(Dish.class))).thenReturn(dish);

        webTestClient
                .post()
                .uri("/api/menu/{menuId}/dishes/{dishId}", 1L, 1L)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(MenuResponseDTO.class)
                .value(response -> {
                    assertEquals(menu.getId(), response.getId());
                    assertEquals(menu.getName(), response.getName());
                    assertEquals(1, response.getDishes().size());
                });

        verify(menuService).saveMenu(any(Menu.class));
        verify(dishService).saveDish(any(Dish.class));
    }

    @Test
    @DisplayName("Agregar Plato a Menú - No Encontrado")
    void testAddDishToMenuNotFound() {
        when(dishService.getDishById(anyLong())).thenReturn(null);

        webTestClient
                .post()
                .uri("/api/menu/{menuId}/dishes/{dishId}", 1L, 1L)
                .exchange()
                .expectStatus().isNotFound();

        verify(dishService).getDishById(anyLong());
    }
}