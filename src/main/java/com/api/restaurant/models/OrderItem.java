package com.api.restaurant.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dish_id")
    private Dish dish;

    private Integer quantity;

    private Double unitPrice;

    private String specialNotes;

    public OrderItem() {
        this.quantity = 1;
    }

    public OrderItem(Dish dish, Integer quantity) {
        this.dish = dish;
        this.quantity = quantity;
        this.unitPrice = dish.getPrice();
    }

    public OrderItem(Dish dish, Integer quantity, String specialNotes) {
        this(dish, quantity);
        this.specialNotes = specialNotes;
    }
}