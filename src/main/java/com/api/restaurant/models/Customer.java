package com.api.restaurant.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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