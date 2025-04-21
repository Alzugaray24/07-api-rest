package com.api.restaurant.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = "`order`")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private Double total;

    private LocalDateTime orderDate;

    private boolean active = true;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    public Order() {
        this.orderDate = LocalDateTime.now();
    }

    // MÉTODOS PARA MANTENER COMPATIBILIDAD

    /**
     * Método de compatibilidad para obtener los platos
     */
    public List<Dish> getDishes() {
        return this.orderItems.stream()
                .map(OrderItem::getDish)
                .collect(Collectors.toList());
    }

    /**
     * Método de compatibilidad para establecer los platos
     */
    public void setDishes(List<Dish> dishes) {
        // Limpiar items existentes
        this.orderItems.clear();

        // Crear OrderItems para cada Dish
        if (dishes != null) {
            for (Dish dish : dishes) {
                OrderItem item = new OrderItem(dish, 1);
                this.addOrderItem(item);
            }
        }
    }

    // Helper method to add order item
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
        recalculateTotal();
    }

    // Helper method to remove order item
    public void removeOrderItem(OrderItem orderItem) {
        orderItems.remove(orderItem);
        orderItem.setOrder(null);
        recalculateTotal();
    }

    // Helper method to calculate total
    public void recalculateTotal() {
        this.total = orderItems.stream()
                .mapToDouble(item -> item.getQuantity() * item.getUnitPrice())
                .sum();
    }
}