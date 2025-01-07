package com.api.restaurant.services;

import com.api.restaurant.models.Dish;
import com.api.restaurant.repositories.DishRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
public class DishService {

    private final DishRepository dishRepository;

    public DishService(DishRepository dishRepository) {
        this.dishRepository = dishRepository;
    }

    public Dish saveDish(Dish dish) {
        return dishRepository.save(dish);
    }

    public Dish getDishById(Long id) {
        return dishRepository.findById(id).orElse(null);
    }

    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    public void deleteDish(Long id) {
        dishRepository.deleteById(id);
    }

    public void updateDish(Long id, Dish updatedDish) {
        dishRepository.findById(id).map(dish -> {
            dish.setName(updatedDish.getName());
            dish.setPrice(updatedDish.getPrice());
            return dishRepository.save(dish);
        }).orElseThrow(() -> new RuntimeException("El plato con el id " + id + " no se ha encontrado"));
    }

}