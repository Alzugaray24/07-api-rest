// DishObserverTest.java
package com.api.restaurant.observer;

import com.api.restaurant.models.Dish;
import com.api.restaurant.models.DishEnum;
import com.api.restaurant.models.Order;
import com.api.restaurant.repositories.OrderRepository;
import com.api.restaurant.services.DishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DishObserverTest {

    private DishObserver dishObserver;
    private OrderRepository orderRepository;
    private DishService dishService;
    private Dish dish;

    @BeforeEach
    void setUp() {
        dish = new Dish();
        dish.setId(1L);
        orderRepository = mock(OrderRepository.class);
        dishService = mock(DishService.class);
        dishObserver = new DishObserver(dish, orderRepository, dishService);
    }

    @Test
    @DisplayName("Update Dish Type to Popular")
    void testUpdateDishTypeToPopular() {
        when(orderRepository.countByDishes_Id(dish.getId())).thenReturn(101L);

        dishObserver.update(new Order());

        verify(dishService).saveDish(dish);
        assertEquals(DishEnum.POPULAR, dish.getType());
    }

    @Test
    @DisplayName("Increase Price for Popular Dish")
    void testIncreasePriceForPopularDish() {
        dish.setType(DishEnum.POPULAR);
        dish.setPrice(10.0);

        dishObserver.update(new Order());

        assertEquals(10.573, dish.getPrice());
    }

    @Test
    @DisplayName("Notify Dish Update")
    void testNotifyDishUpdate() {
        dish.setName("Pizza");

        dishObserver.update(new Order());

        // No assertions needed, just ensure no exceptions are thrown
    }
}