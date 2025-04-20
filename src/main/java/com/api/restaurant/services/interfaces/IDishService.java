package com.api.restaurant.services.interfaces;

import com.api.restaurant.models.Dish;

import java.util.List;

public interface IDishService {
    Dish saveDish(Dish dish);

    Dish getDishById(Long id);

    List<Dish> getAllDishes();

    void deleteDish(Long id);

    Dish updateDish(Long id, Dish updatedDish);
}