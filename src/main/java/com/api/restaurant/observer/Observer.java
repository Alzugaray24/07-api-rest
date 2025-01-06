package com.api.restaurant.observer;

import com.api.restaurant.models.Order;

public interface Observer {
    void update(Order order);
}
