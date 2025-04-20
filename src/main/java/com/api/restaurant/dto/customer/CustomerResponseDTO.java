package com.api.restaurant.dto.customer;

import com.api.restaurant.models.Customer;
import com.api.restaurant.models.CustomerEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class CustomerResponseDTO {
    private Long id;
    private String name;
    private String email;
    private boolean active;
    private CustomerEnum type;
    private List<OrderSummaryDTO> orders;

    public CustomerResponseDTO(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.email = customer.getEmail();
        this.active = customer.isActive();
        this.type = customer.getType();

        // Solo incluir órdenes si el cliente tiene alguna y si no es null
        if (customer.getOrders() != null && !customer.getOrders().isEmpty()) {
            try {
                this.orders = customer.getOrders().stream()
                        .map(OrderSummaryDTO::new)
                        .collect(Collectors.toList());
            } catch (Exception e) {
                // Si hay algún error, no incluir órdenes
                this.orders = null;
            }
        }
    }

    public CustomerResponseDTO() {
    }

    // DTO interno para representar un resumen de la orden
    @Getter
    @Setter
    public static class OrderSummaryDTO {
        private Long id;
        private Double total;
        private String orderDate;

        public OrderSummaryDTO(com.api.restaurant.models.Order order) {
            this.id = order.getId();
            this.total = order.getTotal();
            if (order.getOrderDate() != null) {
                this.orderDate = order.getOrderDate().toString();
            }
        }
    }
}