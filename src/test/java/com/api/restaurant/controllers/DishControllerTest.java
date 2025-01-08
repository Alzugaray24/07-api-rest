package com.api.restaurant.controllers;

import com.api.restaurant.dto.dish.DishRequestDTO;
import com.api.restaurant.dto.dish.DishResponseDTO;
import com.api.restaurant.models.Dish;
import com.api.restaurant.services.DishService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class DishControllerTest {

    private final WebTestClient webTestClient;
    private final DishService dishService;

    public DishControllerTest() {
        dishService = mock(DishService.class);
        webTestClient = WebTestClient.bindToController(new DishController(dishService)).build();
    }

    @Test
    @DisplayName("Save Dish")
    void testSaveDish() {
        DishRequestDTO requestDTO = new DishRequestDTO();
        requestDTO.setName("Pizza");
        requestDTO.setPrice(12.99);

        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Pizza");
        dish.setPrice(12.99);

        when(dishService.saveDish(any(Dish.class))).thenReturn(dish);

        webTestClient
                .post()
                .uri("/api/dish")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DishResponseDTO.class)
                .value(response -> {
                    assertEquals(dish.getId(), response.getId());
                    assertEquals(dish.getName(), response.getName());
                    assertEquals(dish.getPrice(), response.getPrice());
                });

        Mockito.verify(dishService).saveDish(any(Dish.class));
    }

    @Test
    @DisplayName("Get Dish by ID")
    void testGetDishById() {
        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Pizza");
        dish.setPrice(12.99);

        when(dishService.getDishById(anyLong())).thenReturn(dish);

        webTestClient
                .get()
                .uri("/api/dish/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DishResponseDTO.class)
                .value(response -> {
                    assertEquals(dish.getId(), response.getId());
                    assertEquals(dish.getName(), response.getName());
                    assertEquals(dish.getPrice(), response.getPrice());
                });

        Mockito.verify(dishService).getDishById(anyLong());
    }

    @Test
    @DisplayName("Get All Dishes")
    void testGetAllDishes() {
        List<Dish> dishes = List.of(
                new Dish("Pizza", 12.99),
                new Dish("Pasta", 8.99),
                new Dish("Salad", 5.99)
        );

        when(dishService.getAllDishes()).thenReturn(dishes);

        webTestClient
                .get()
                .uri("/api/dish")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(DishResponseDTO.class)
                .hasSize(3)
                .value(response -> {
                    assertEquals("Pizza", response.get(0).getName());
                    assertEquals(12.99, response.get(0).getPrice());
                    assertEquals("Pasta", response.get(1).getName());
                    assertEquals(8.99, response.get(1).getPrice());
                    assertEquals("Salad", response.get(2).getName());
                    assertEquals(5.99, response.get(2).getPrice());
                });

        Mockito.verify(dishService).getAllDishes();
    }

    @Test
    @DisplayName("Update Dish")
    void testUpdateDish() {
        DishRequestDTO requestDTO = new DishRequestDTO();
        requestDTO.setName("Pizza");
        requestDTO.setPrice(12.99);

        Dish dish = new Dish();
        dish.setId(1L);
        dish.setName("Pizza");
        dish.setPrice(12.99);

        when(dishService.updateDish(anyLong(), any(Dish.class))).thenReturn(dish);

        webTestClient
                .put()
                .uri("/api/dish/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(DishResponseDTO.class)
                .value(response -> {
                    assertEquals(dish.getId(), response.getId());
                    assertEquals(dish.getName(), response.getName());
                    assertEquals(dish.getPrice(), response.getPrice());
                });

        Mockito.verify(dishService).updateDish(anyLong(), any(Dish.class));
    }

    @Test
    @DisplayName("Delete Dish")
    void testDeleteDish() {
        doNothing().when(dishService).deleteDish(anyLong());

        webTestClient
                .delete()
                .uri("/api/dish/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();

        Mockito.verify(dishService).deleteDish(anyLong());
    }
}