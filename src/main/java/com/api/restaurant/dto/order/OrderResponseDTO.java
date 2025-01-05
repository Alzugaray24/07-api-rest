package com.api.restaurant.dto.order;

import com.api.restaurant.dto.customer.CustomerResponseDTO;
import com.api.restaurant.dto.dish.DishResponseDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderResponseDTO {
    private Long id;
    private CustomerResponseDTO customer;
    private List<DishResponseDTO> dishes;
}