package com.api.restaurant.controllers;

import com.api.restaurant.dto.order.OrderRequestDTO;
import com.api.restaurant.dto.order.OrderResponseDTO;
import com.api.restaurant.models.Customer;
import com.api.restaurant.models.Dish;
import com.api.restaurant.models.Order;
import com.api.restaurant.services.OrderService;
import com.api.restaurant.services.CustomerService;
import com.api.restaurant.services.DishService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    private WebTestClient webTestClient;
    private OrderService orderService;
    private CustomerService customerService;
    private DishService dishService;

    @BeforeEach
    void setUp() {
        orderService = mock(OrderService.class);
        customerService = mock(CustomerService.class);
        dishService = mock(DishService.class);
        webTestClient = WebTestClient.bindToController(new OrderController(orderService, customerService, dishService)).build();
    }

    @Test
    @DisplayName("Save Order")
    void testSaveOrder() {
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setCustomerId(1L);
        requestDTO.setDishIds(List.of(1L, 2L));

        Order order = new Order();
        order.setId(1L);
        Customer customer = new Customer();
        customer.setId(1L);
        Dish dish1 = new Dish();
        dish1.setId(1L);
        Dish dish2 = new Dish();
        dish2.setId(2L);
        order.setCustomer(customer);
        order.setDishes(List.of(dish1, dish2));

        when(customerService.getCustomerById(anyLong())).thenReturn(customer);
        when(dishService.getDishById(anyLong())).thenReturn(dish1, dish2);
        when(orderService.saveOrder(any(Order.class))).thenReturn(order);

        webTestClient
                .post()
                .uri("/api/order")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(OrderResponseDTO.class)
                .value(response -> {
                    assertEquals(order.getId(), response.getId());
                    assertEquals(customer.getId(), response.getCustomer().getId());
                    assertEquals(2, response.getDishes().size());
                });

        verify(orderService).saveOrder(argThat(argument ->
                argument.getCustomer().getId().equals(requestDTO.getCustomerId())
        ));
    }

    @Test
    @DisplayName("Get Order by ID")
    void testGetOrderById() {
        Order order = new Order();
        order.setId(1L);
        order.setDishes(new ArrayList<>());
        Customer customer = new Customer();
        customer.setId(1L);
        order.setCustomer(customer);

        when(orderService.getOrderById(anyLong())).thenReturn(order);

        webTestClient
                .get()
                .uri("/api/order/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(OrderResponseDTO.class)
                .value(response -> {
                    assertEquals(order.getId(), response.getId());
                    assertEquals(customer.getId(), response.getCustomer().getId());
                });

        verify(orderService).getOrderById(anyLong());
    }

    @Test
    @DisplayName("Update Order")
    void testUpdateOrder() {
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setCustomerId(1L);
        requestDTO.setDishIds(List.of(1L, 2L));

        Order order = new Order();
        order.setId(1L);
        Customer customer = new Customer();
        customer.setId(1L);
        Dish dish1 = new Dish();
        dish1.setId(1L);
        Dish dish2 = new Dish();
        dish2.setId(2L);
        order.setCustomer(customer);
        order.setDishes(List.of(dish1, dish2));

        when(customerService.getCustomerById(anyLong())).thenReturn(customer);
        when(dishService.getDishById(anyLong())).thenReturn(dish1, dish2);
        when(orderService.updateOrder(anyLong(), any(Order.class))).thenReturn(order);

        webTestClient
                .put()
                .uri("/api/order/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(OrderResponseDTO.class)
                .value(response -> {
                    assertEquals(order.getId(), response.getId());
                    assertEquals(customer.getId(), response.getCustomer().getId());
                });

        verify(orderService).updateOrder(anyLong(), argThat(argument ->
                argument.getCustomer().getId().equals(requestDTO.getCustomerId())
        ));
    }

    @Test
    @DisplayName("Delete Order")
    void testDeleteOrder() {
        doNothing().when(orderService).deleteOrder(anyLong());

        webTestClient
                .delete()
                .uri("/api/order/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();

        verify(orderService).deleteOrder(anyLong());
    }
}