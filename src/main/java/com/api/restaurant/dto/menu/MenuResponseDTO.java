package com.api.restaurant.dto.menu;

import com.api.restaurant.dto.dish.DishResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuResponseDTO {
    private Long id;
    private String name;
    private List<DishResponseDTO> dishes;
}