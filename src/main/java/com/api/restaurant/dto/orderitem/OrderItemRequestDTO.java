package com.api.restaurant.dto.orderitem;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequestDTO {
    private Long dishId;
    private Integer quantity;
    private String specialNotes;

    public OrderItemRequestDTO() {
        this.quantity = 1;
    }
}