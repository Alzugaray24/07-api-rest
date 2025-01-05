package com.api.restaurant.services;

import com.api.restaurant.models.Order;
import com.api.restaurant.repositories.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public void updateOrder(Long id, Order updatedOrder) {
        orderRepository.findById(id).map(order -> {
            order.setCustomer(updatedOrder.getCustomer());
            order.setDishes(updatedOrder.getDishes());
            return orderRepository.save(order);
        }).orElseThrow(() -> new RuntimeException("El pedido con el id " + id + " no se ha encontrado"));
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}