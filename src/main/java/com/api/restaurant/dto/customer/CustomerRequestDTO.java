package com.api.restaurant.dto.customer;

import com.api.restaurant.models.CustomerEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequestDTO {
    private String name;

    public CustomerEnum getType() {
        return CustomerEnum.NORMAL;
    }
}