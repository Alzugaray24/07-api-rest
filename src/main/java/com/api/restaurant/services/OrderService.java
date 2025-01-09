package com.api.restaurant.services;

import com.api.restaurant.models.Order;
import com.api.restaurant.observer.CustomerObserver;
import com.api.restaurant.observer.DishObserver;
import com.api.restaurant.observer.Observable;
import com.api.restaurant.observer.Observer;
import com.api.restaurant.repositories.OrderRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService implements Observable {

    private final OrderRepository orderRepository;
    @Getter
    private final List<Observer> observers;
    private final CustomerService customerService;
    private final DishService dishService;



    public OrderService(OrderRepository orderRepository, List<Observer> observers, CustomerService customerService, DishService dishService) {
        this.orderRepository = orderRepository;
        this.observers = observers;
        this.customerService = customerService;
        this.dishService = dishService;
    }

    public Order saveOrder(Order order) {
        CustomerObserver customerObserver = new CustomerObserver(order.getCustomer(), orderRepository, customerService);
        if (!observers.contains(customerObserver)) {
            addObserver(customerObserver);
        }

        order.getDishes().forEach(dish -> {
            DishObserver dishObserver = new DishObserver(dish, orderRepository, dishService);
            if (!observers.contains(dishObserver)) {
                addObserver(dishObserver);
            }
        });

        Order savedOrder = orderRepository.save(order);
        notifyObservers();
        return savedOrder;
    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

    public Order updateOrder(Long id, Order updatedOrder) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setCustomer(updatedOrder.getCustomer());
                    order.setDishes(updatedOrder.getDishes());
                    return orderRepository.save(order);
                })
                .orElseThrow(() -> new RuntimeException("El pedido con el id " + id + " no se ha encontrado"));
    }


    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        List<Order> orders = orderRepository.findAll();
        if (!orders.isEmpty()) {
            Order latestOrder = orders.get(orders.size() - 1);
            observers.forEach(observer -> observer.update(latestOrder));
        }
        orderRepository.findAll();
    }

}