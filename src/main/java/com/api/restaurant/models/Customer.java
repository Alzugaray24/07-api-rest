package com.api.restaurant.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private boolean active = true;

    @Enumerated(EnumType.STRING)
    private CustomerEnum type;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public Customer() {
    }

    public Customer(String name, String email) {
        this.name = name;
        this.email = email;
        this.type = CustomerEnum.NORMAL;
        this.active = true;
    }

    // Helper method to add order
    public void addOrder(Order order) {
        orders.add(order);
        order.setCustomer(this);
    }

    // Helper method to remove order
    public void removeOrder(Order order) {
        orders.remove(order);
        order.setCustomer(null);
    }
}