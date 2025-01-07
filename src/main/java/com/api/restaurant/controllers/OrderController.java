package com.api.restaurant.controllers;

import com.api.restaurant.dto.order.OrderRequestDTO;
import com.api.restaurant.dto.order.OrderResponseDTO;
import com.api.restaurant.dto.customer.CustomerResponseDTO;
import com.api.restaurant.dto.dish.DishResponseDTO;
import com.api.restaurant.models.Order;
import com.api.restaurant.models.Customer;
import com.api.restaurant.models.Dish;
import com.api.restaurant.services.OrderService;
import com.api.restaurant.services.CustomerService;
import com.api.restaurant.services.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService service;
    private final CustomerService customerService;
    private final DishService dishService;

    @Autowired
    public OrderController(OrderService service, CustomerService customerService, DishService dishService) {
        this.service = service;
        this.customerService = customerService;
        this.dishService = dishService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> saveOrder(@RequestBody OrderRequestDTO orderRequest) {
        Order order = new Order();
        Customer customer = customerService.getCustomerById(orderRequest.getCustomerId());
        List<Dish> dishes = orderRequest.getDishIds().stream()
                .map(dishService::getDishById)
                .collect(Collectors.toList());
        order.setCustomer(customer);
        order.setDishes(dishes);
        Order savedOrder = service.saveOrder(order);
        OrderResponseDTO response = convertToOrderResponseDTO(savedOrder);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        Order order = service.getOrderById(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        OrderResponseDTO response = convertToOrderResponseDTO(order);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<Order> orders = service.getAllOrders();
        List<OrderResponseDTO> responses = orders.stream()
                .map(this::convertToOrderResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable Long id, @RequestBody OrderRequestDTO orderRequest) {
        Order updatedOrder = new Order();
        Customer customer = customerService.getCustomerById(orderRequest.getCustomerId());
        List<Dish> dishes = orderRequest.getDishIds().stream()
                .map(dishService::getDishById)
                .collect(Collectors.toList());
        updatedOrder.setCustomer(customer);
        updatedOrder.setDishes(dishes);
        service.updateOrder(id, updatedOrder);
        OrderResponseDTO response = convertToOrderResponseDTO(updatedOrder);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        service.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    private OrderResponseDTO convertToOrderResponseDTO(Order order) {
        OrderResponseDTO response = new OrderResponseDTO();
        response.setId(order.getId());
        response.setCustomer(new CustomerResponseDTO(order.getCustomer()));
        response.setDishes(order.getDishes().stream()
                .map(DishResponseDTO::new)
                .collect(Collectors.toList()));
        return response;
    }
}