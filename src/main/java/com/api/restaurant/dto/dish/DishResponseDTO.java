package com.api.restaurant.dto.dish;

import com.api.restaurant.models.Dish;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishResponseDTO {
    private Long id;
    private String name;
    private double price;

    public DishResponseDTO(Dish dish) {
        this.id = dish.getId();
        this.name = dish.getName();
        this.price = dish.getPrice();
    }
}