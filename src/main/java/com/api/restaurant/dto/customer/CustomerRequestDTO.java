package com.api.restaurant.dto.customer;

import com.api.restaurant.models.CustomerEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequestDTO {
    private String name;
    private String email;
    private Boolean active;
    private CustomerEnum type;

    public CustomerRequestDTO() {
        this.type = CustomerEnum.NORMAL;
        this.active = true;
    }
}