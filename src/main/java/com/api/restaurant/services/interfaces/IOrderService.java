package com.api.restaurant.services.interfaces;

import com.api.restaurant.models.Order;

import java.util.List;

public interface IOrderService {
    Order saveOrder(Order order);

    Order getOrderById(Long id);

    List<Order> getAllOrders();

    void deleteOrder(Long id);

    Order updateOrder(Long id, Order updatedOrder);

    Order setOrderStatus(Long id, boolean active);

    List<Order> getActiveOrders();
}