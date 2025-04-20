package com.api.restaurant.dto.order;

import com.api.restaurant.dto.orderitem.OrderItemRequestDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDTO {
    private Long customerId;
    private List<OrderItemRequestDTO> items;

    public OrderRequestDTO() {
    }
}