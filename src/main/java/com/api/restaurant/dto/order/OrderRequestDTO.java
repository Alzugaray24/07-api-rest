package com.api.restaurant.dto.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDTO {
    private Long customerId;
    private List<Long> dishIds;
}