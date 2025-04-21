package com.api.restaurant.services.interfaces;

import com.api.restaurant.models.Dish;

import java.util.List;

public interface IDishService {
    Dish saveDish(Dish dish);

    Dish getDishById(Long id);

    List<Dish> getAllDishes();

    List<Dish> getActiveDishes();

    void deleteDish(Long id);

    Dish setDishStatus(Long id, boolean active);

    Dish updateDish(Long id, Dish updatedDish);
}