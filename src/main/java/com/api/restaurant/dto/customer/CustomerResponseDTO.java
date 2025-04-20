package com.api.restaurant.dto.customer;

import com.api.restaurant.models.Customer;
import com.api.restaurant.models.CustomerEnum;
import com.api.restaurant.models.Order;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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

        // Modificación para garantizar que las órdenes se inicialicen correctamente
        if (customer.getOrders() != null) {
            try {
                // Forzar inicialización para evitar LazyInitializationException
                List<Order> customerOrders = new ArrayList<>(customer.getOrders());

                if (!customerOrders.isEmpty()) {
                    this.orders = customerOrders.stream()
                            .map(OrderSummaryDTO::new)
                            .collect(Collectors.toList());
                } else {
                    this.orders = new ArrayList<>();
                }
            } catch (Exception e) {
                // Si hay algún error, inicializar como lista vacía en lugar de null
                this.orders = new ArrayList<>();
            }
        } else {
            // Inicializar como lista vacía en lugar de null
            this.orders = new ArrayList<>();
        }
    }

    public CustomerResponseDTO() {
        this.orders = new ArrayList<>();
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