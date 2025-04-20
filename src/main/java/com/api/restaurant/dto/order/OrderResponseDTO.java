package com.api.restaurant.dto.order;

import com.api.restaurant.dto.customer.CustomerResponseDTO;
import com.api.restaurant.dto.orderitem.OrderItemResponseDTO;
import com.api.restaurant.models.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderResponseDTO {
    private Long id;
    private CustomerResponseDTO customer;
    private List<OrderItemResponseDTO> items;
    private Double total;
    private LocalDateTime orderDate;

    public OrderResponseDTO() {
    }

    public OrderResponseDTO(Order order) {
        this.id = order.getId();
        this.customer = new CustomerResponseDTO(order.getCustomer());
        this.items = order.getOrderItems().stream()
                .map(OrderItemResponseDTO::new)
                .collect(Collectors.toList());
        this.total = order.getTotal();
        this.orderDate = order.getOrderDate();
    }
}