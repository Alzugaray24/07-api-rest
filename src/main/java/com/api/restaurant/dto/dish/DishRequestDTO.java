package com.api.restaurant.dto.dish;

import com.api.restaurant.models.DishEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishRequestDTO {
    private String name;
    private double price;
    private DishEnum type;

    public DishRequestDTO() {
    }
}