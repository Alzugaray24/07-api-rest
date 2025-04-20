package com.api.restaurant.dto.dish;

import com.api.restaurant.models.Dish;
import com.api.restaurant.models.DishEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishResponseDTO {
    private Long id;
    private String name;
    private double price;
    private DishEnum type;

    public DishResponseDTO(Dish dish) {
        this.id = dish.getId();
        this.name = dish.getName();
        this.price = dish.getPrice();
        this.type = dish.getType();
    }

    public DishResponseDTO() {
    }

}