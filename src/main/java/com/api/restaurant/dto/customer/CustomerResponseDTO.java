package com.api.restaurant.dto.customer;

import com.api.restaurant.models.Customer;
import com.api.restaurant.models.CustomerEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponseDTO {
    private Long id;
    private String name;
    private CustomerEnum type;

    public CustomerResponseDTO(Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
        this.type = customer.getType();
    }

    public CustomerResponseDTO() {
    }
}