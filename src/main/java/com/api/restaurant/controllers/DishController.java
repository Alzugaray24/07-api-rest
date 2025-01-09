package com.api.restaurant.controllers;

import com.api.restaurant.dto.dish.DishRequestDTO;
import com.api.restaurant.dto.dish.DishResponseDTO;
import com.api.restaurant.models.Dish;
import com.api.restaurant.services.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dish")
public class DishController {

    private final DishService service;

    @Autowired
    public DishController(DishService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DishResponseDTO> saveDish(@RequestBody DishRequestDTO dishRequest) {
        Dish dish = new Dish();
        dish.setName(dishRequest.getName());
        dish.setPrice(dishRequest.getPrice());
        Dish savedDish = service.saveDish(dish);
        DishResponseDTO response = new DishResponseDTO(savedDish);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DishResponseDTO> getDishById(@PathVariable Long id) {
        Dish dish = service.getDishById(id);
        if (dish == null) {
            return ResponseEntity.notFound().build();
        }
        DishResponseDTO response = new DishResponseDTO(dish);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<DishResponseDTO>> getAllDishes() {
        List<Dish> dishes = service.getAllDishes();
        List<DishResponseDTO> responses = dishes.stream()
                .map(DishResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DishResponseDTO> updateDish(@PathVariable Long id, @RequestBody DishRequestDTO dishRequest) {
        Dish updatedDish = new Dish();
        updatedDish.setName(dishRequest.getName());
        updatedDish.setPrice(dishRequest.getPrice());
        Dish newDish = service.updateDish(id, updatedDish);
        if (newDish == null) {
            return ResponseEntity.notFound().build();
        }
        DishResponseDTO response = new DishResponseDTO(newDish);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable Long id) {
        try {
            service.deleteDish(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}