// CustomerObserverTest.java
package com.api.restaurant.observer;

import com.api.restaurant.models.Customer;
import com.api.restaurant.models.CustomerEnum;
import com.api.restaurant.models.Dish;
import com.api.restaurant.models.Order;
import com.api.restaurant.repositories.OrderRepository;
import com.api.restaurant.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CustomerObserverTest {

    private CustomerObserver customerObserver;
    private OrderRepository orderRepository;
    private CustomerService customerService;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        orderRepository = mock(OrderRepository.class);
        customerService = mock(CustomerService.class);
        customerObserver = new CustomerObserver(customer, orderRepository, customerService);
    }

    @Test
    @DisplayName("Actualizar Tipo de Cliente a Frecuente")
    void testUpdateCustomerTypeToFrequent() {
        when(orderRepository.countByCustomerId(customer.getId())).thenReturn(10L);

        Order order = new Order();
        order.setDishes(List.of(new Dish())); // Ensure dishes are not null
        customerObserver.update(order);

        verify(customerService).saveCustomer(customer);
        assertEquals(CustomerEnum.FRECUENT, customer.getType());
    }

    @Test
    @DisplayName("Aplicar Descuento a Cliente Frecuente")
    void testApplyDiscountToFrequentCustomer() {
        customer.setType(CustomerEnum.FRECUENT);
        Order order = new Order();
        Dish dish1 = new Dish();
        dish1.setPrice(10.0);
        Dish dish2 = new Dish();
        dish2.setPrice(20.0);
        order.setDishes(List.of(dish1, dish2));

        customerObserver.update(order);

        assertEquals(29.286, order.getTotal(), 0.001); // Adjust precision
    }

    @Test
    @DisplayName("Notificar Cliente")
    void testNotifyCustomer() {
        Order order = new Order();
        order.setId(1L);
        customer.setName("John Doe");

        customerObserver.update(order);

    }
}