package com.api.restaurant.services;

import com.api.restaurant.models.Dish;
import com.api.restaurant.repositories.DishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class DishServiceTest {

    private DishRepository dishRepository;
    private DishService dishService;

    @BeforeEach
    void setUp() {
        dishRepository = mock(DishRepository.class);
        dishService = new DishService(dishRepository);
    }

    @Test
    @DisplayName("Guardar Plato")
    void testSaveDish() {
        Dish dish = new Dish("Pizza", 12.99);
        when(dishRepository.save(any(Dish.class))).thenReturn(dish);

        Dish savedDish = dishService.saveDish(dish);

        assertEquals("Pizza", savedDish.getName());
        assertEquals(12.99, savedDish.getPrice());
        verify(dishRepository, times(1)).save(any(Dish.class));
    }

    @Test
    @DisplayName("Obtener Plato por ID")
    void testGetDishById() {
        Dish dish = new Dish("Pizza", 12.99);
        when(dishRepository.findById(anyLong())).thenReturn(Optional.of(dish));

        Dish foundDish = dishService.getDishById(1L);

        assertEquals("Pizza", foundDish.getName());
        assertEquals(12.99, foundDish.getPrice());
        verify(dishRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Obtener Todos los Platos")
    void testGetAllDishes() {
        List<Dish> dishes = List.of(
                new Dish("Pizza", 12.99),
                new Dish("Pasta", 8.99),
                new Dish("Ensalada", 5.99)
        );
        when(dishRepository.findAll()).thenReturn(dishes);

        List<Dish> foundDishes = dishService.getAllDishes();

        assertEquals(3, foundDishes.size());
        assertEquals("Pizza", foundDishes.get(0).getName());
        verify(dishRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Actualizar Plato")
    void testUpdateDish() {
        Dish existingDish = new Dish("Pizza", 12.99);
        when(dishRepository.findById(anyLong())).thenReturn(Optional.of(existingDish));
        when(dishRepository.save(any(Dish.class))).thenReturn(existingDish);

        Dish updatedDish = new Dish("Pizza Actualizada", 15.99);
        Dish result = dishService.updateDish(1L, updatedDish);

        assertEquals("Pizza Actualizada", result.getName());
        assertEquals(15.99, result.getPrice());
        verify(dishRepository, times(1)).findById(anyLong());
        verify(dishRepository, times(1)).save(any(Dish.class));
    }

    @Test
    @DisplayName("Eliminar Plato")
    void testDeleteDish() {
        doNothing().when(dishRepository).deleteById(anyLong());

        dishService.deleteDish(1L);

        verify(dishRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Actualizar Plato - No Encontrado")
    void testUpdateDishNotFound() {
        when(dishRepository.findById(anyLong())).thenReturn(Optional.empty());

        Dish updatedDish = new Dish("Pizza Actualizada", 15.99);

        assertThrows(RuntimeException.class, () -> dishService.updateDish(1L, updatedDish));
        verify(dishRepository, times(1)).findById(anyLong());
    }
}