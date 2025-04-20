package com.api.restaurant.controllers;

import com.api.restaurant.dto.order.OrderRequestDTO;
import com.api.restaurant.dto.order.OrderResponseDTO;
import com.api.restaurant.models.Order;
import com.api.restaurant.models.Customer;
import com.api.restaurant.models.Dish;
import com.api.restaurant.models.OrderItem;
import com.api.restaurant.services.OrderService;
import com.api.restaurant.services.CustomerService;
import com.api.restaurant.services.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;
    private final CustomerService customerService;
    private final DishService dishService;

    @Autowired
    public OrderController(OrderService orderService, CustomerService customerService, DishService dishService) {
        this.orderService = orderService;
        this.customerService = customerService;
        this.dishService = dishService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> saveOrder(@RequestBody OrderRequestDTO orderRequest) {
        // Get the customer
        Customer customer = customerService.getCustomerById(orderRequest.getCustomerId());
        if (customer == null) {
            return ResponseEntity.badRequest().build();
        }

        // Create a new order
        Order order = new Order();
        order.setCustomer(customer);

        // Process order items
        List<OrderItem> orderItems = new ArrayList<>();
        for (var itemRequest : orderRequest.getItems()) {
            Dish dish = dishService.getDishById(itemRequest.getDishId());
            if (dish == null) {
                continue;
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setDish(dish);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(dish.getPrice());
            orderItem.setSpecialNotes(itemRequest.getSpecialNotes());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.recalculateTotal();

        // Save the order
        Order savedOrder = orderService.saveOrder(order);
        OrderResponseDTO response = new OrderResponseDTO(savedOrder);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        OrderResponseDTO response = new OrderResponseDTO(order);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderResponseDTO> response = orders.stream()
                .map(OrderResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable Long id,
            @RequestBody OrderRequestDTO orderRequest) {
        // Check if order exists
        Order existingOrder = orderService.getOrderById(id);
        if (existingOrder == null) {
            return ResponseEntity.notFound().build();
        }

        // Get the customer
        Customer customer = customerService.getCustomerById(orderRequest.getCustomerId());
        if (customer == null) {
            return ResponseEntity.badRequest().build();
        }

        // Create order items
        List<OrderItem> orderItems = new ArrayList<>();
        for (var itemRequest : orderRequest.getItems()) {
            Dish dish = dishService.getDishById(itemRequest.getDishId());
            if (dish == null) {
                continue;
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setDish(dish);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setUnitPrice(dish.getPrice());
            orderItem.setSpecialNotes(itemRequest.getSpecialNotes());
            orderItems.add(orderItem);
        }

        // Update the order
        Order updatedOrder = new Order();
        updatedOrder.setCustomer(customer);
        updatedOrder.setOrderItems(orderItems);

        Order newOrder = orderService.updateOrder(id, updatedOrder);
        OrderResponseDTO response = new OrderResponseDTO(newOrder);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}