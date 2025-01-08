package com.api.restaurant.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Enumerated(EnumType.STRING)
    private CustomerEnum type;

    public Customer() {
        this.type = CustomerEnum.NORMAL;
    }

    public Customer(String name) {
        this.name = name;
        this.type = CustomerEnum.NORMAL;
    }
}