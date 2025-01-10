package com.api.restaurant.services;

import com.api.restaurant.models.Customer;
import com.api.restaurant.models.Dish;
import com.api.restaurant.models.Order;
import com.api.restaurant.observer.Observer;
import com.api.restaurant.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderService orderService;
    private OrderRepository orderRepository;
    private CustomerService customerService;
    private DishService dishService;
    private List<Observer> observers;

    @BeforeEach
    void setUp() {
        orderRepository = mock(OrderRepository.class);
        customerService = mock(CustomerService.class);
        dishService = mock(DishService.class);
        observers = new ArrayList<>();
        orderService = new OrderService(orderRepository, observers, customerService, dishService);
    }

    @Test
    @DisplayName("Guardar Pedido")
    void testSaveOrder() {
        Order order = new Order();
        Customer customer = new Customer();
        customer.setId(1L);
        Dish dish1 = new Dish();
        dish1.setId(1L);
        Dish dish2 = new Dish();
        dish2.setId(2L);
        order.setCustomer(customer);
        order.setDishes(List.of(dish1, dish2));

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order savedOrder = orderService.saveOrder(order);

        assertNotNull(savedOrder);
        assertEquals(order.getCustomer(), savedOrder.getCustomer());
        assertEquals(order.getDishes(), savedOrder.getDishes());
        verify(orderRepository).save(order);
        verify(orderRepository, times(2)).findAll();
    }

    @Test
    @DisplayName("Obtener Pedido por ID")
    void testGetOrderById() {
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(order));

        Order foundOrder = orderService.getOrderById(1L);

        assertNotNull(foundOrder);
        assertEquals(order.getId(), foundOrder.getId());
        verify(orderRepository).findById(1L);
    }

    @Test
    @DisplayName("Obtener Pedido por ID - No Encontrado")
    void testGetOrderByIdNotFound() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        Order foundOrder = orderService.getOrderById(1L);

        assertNull(foundOrder);
        verify(orderRepository).findById(1L);
    }

    @Test
    @DisplayName("Eliminar Pedido")
    void testDeleteOrder() {
        doNothing().when(orderRepository).deleteById(anyLong());

        orderService.deleteOrder(1L);

        verify(orderRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Actualizar Pedido")
    void testUpdateOrder() {
        Order existingOrder = new Order();
        existingOrder.setId(1L);
        Customer customer = new Customer();
        customer.setId(1L);
        Dish dish1 = new Dish();
        dish1.setId(1L);
        Dish dish2 = new Dish();
        dish2.setId(2L);
        existingOrder.setCustomer(customer);
        existingOrder.setDishes(List.of(dish1, dish2));

        Order updatedOrder = new Order();
        updatedOrder.setId(1L);
        updatedOrder.setCustomer(customer);
        updatedOrder.setDishes(List.of(dish1, dish2));

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        Order result = orderService.updateOrder(1L, updatedOrder);

        assertNotNull(result);
        assertEquals(updatedOrder.getCustomer(), result.getCustomer());
        assertEquals(updatedOrder.getDishes(), result.getDishes());
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(existingOrder);
    }

    @Test
    @DisplayName("Actualizar Pedido - No Encontrado")
    void testUpdateOrderNotFound() {
        Order updatedOrder = new Order();
        updatedOrder.setId(1L);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            orderService.updateOrder(1L, updatedOrder);
        });

        assertEquals("El pedido con el id 1 no se ha encontrado", exception.getMessage());
        verify(orderRepository).findById(1L);
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Agregar Observador")
    void testAddObserver() {
        Observer observer = mock(Observer.class);
        orderService.addObserver(observer);

        assertTrue(orderService.getObservers().contains(observer));
    }

    @Test
    @DisplayName("Notificar Observadores")
    void testNotifyObservers() {
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findAll()).thenReturn(List.of(order));

        Observer observer = mock(Observer.class);
        orderService.addObserver(observer);

        orderService.notifyObservers();

        verify(observer).update(order);
    }
}