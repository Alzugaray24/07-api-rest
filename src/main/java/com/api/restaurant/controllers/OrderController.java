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
import java.util.Objects;
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
        Order order = new Order();
        Customer customer = customerService.getCustomerById(orderRequest.getCustomerId());
        List<Dish> dishes = orderRequest.getDishIds().stream()
                .map(dishService::getDishById)
                .collect(Collectors.toList());
        order.setCustomer(customer);
        order.setDishes(dishes);
        order.setTotal(dishes.stream()
                .mapToDouble(Dish::getPrice)
                .sum());
        Order savedOrder = orderService.saveOrder(order);
        OrderResponseDTO response = convertToOrderResponseDTO(savedOrder);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        OrderResponseDTO response = convertToOrderResponseDTO(order);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> updateOrder(@PathVariable Long id, @RequestBody OrderRequestDTO orderRequest) {
        Customer customer = getCustomer(orderRequest.getCustomerId());
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }

        List<Dish> dishes = getDishes(orderRequest.getDishIds());
        if (dishes.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Order updatedOrder = createUpdatedOrder(id, customer, dishes);
        Order newOrder = orderService.updateOrder(id, updatedOrder);
        if (newOrder == null) {
            return ResponseEntity.notFound().build();
        }

        OrderResponseDTO response = convertToOrderResponseDTO(newOrder);
        return ResponseEntity.ok(response);
    }

    private Customer getCustomer(Long customerId) {
        return customerService.getCustomerById(customerId);
    }

    private List<Dish> getDishes(List<Long> dishIds) {
        return dishIds.stream()
                .map(dishService::getDishById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private Order createUpdatedOrder(Long id, Customer customer, List<Dish> dishes) {
        Order order = new Order();
        order.setId(id);
        order.setCustomer(customer);
        order.setDishes(dishes);
        order.setTotal(dishes.stream()
                .mapToDouble(Dish::getPrice)
                .sum());
        return order;
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

    private OrderResponseDTO convertToOrderResponseDTO(Order order) {
        OrderResponseDTO response = new OrderResponseDTO();
        response.setId(order.getId());
        response.setCustomer(new CustomerResponseDTO(order.getCustomer()));
        response.setDishes(order.getDishes().stream()
                .map(DishResponseDTO::new)
                .collect(Collectors.toList()));
        response.setTotal(order.getTotal());
        return response;
    }
}