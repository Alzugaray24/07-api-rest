package com.api.restaurant.dto.orderitem;

import com.api.restaurant.dto.dish.DishResponseDTO;
import com.api.restaurant.models.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponseDTO {
    private Long id;
    private DishResponseDTO dish;
    private Integer quantity;
    private Double unitPrice;
    private String specialNotes;

    public OrderItemResponseDTO() {
    }

    public OrderItemResponseDTO(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.dish = new DishResponseDTO(orderItem.getDish());
        this.quantity = orderItem.getQuantity();
        this.unitPrice = orderItem.getUnitPrice();
        this.specialNotes = orderItem.getSpecialNotes();
    }
}