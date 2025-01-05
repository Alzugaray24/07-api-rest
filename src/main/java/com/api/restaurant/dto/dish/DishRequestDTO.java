package com.api.restaurant.dto.dish;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DishRequestDTO {
    private String name;
    private double price;
}