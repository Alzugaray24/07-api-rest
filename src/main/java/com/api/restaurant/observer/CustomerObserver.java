package com.api.restaurant.observer;

import com.api.restaurant.models.Customer;
import com.api.restaurant.models.CustomerEnum;
import com.api.restaurant.models.Order;
import com.api.restaurant.repositories.OrderRepository;
import com.api.restaurant.services.CustomerService;
import lombok.Getter;

public class CustomerObserver implements Observer {

    @Getter
    private final Customer customer;
    private final OrderRepository orderRepository;
    private final CustomerService customerService;

    public CustomerObserver(Customer customer, OrderRepository orderRepository, CustomerService customerService) {
        this.customer = customer;
        this.orderRepository = orderRepository;
        this.customerService = customerService;
    }

    @Override
    public void update(Order order) {
        long orderCount = orderRepository.countByCustomerId(customer.getId());
        if (orderCount >= 10) {
            customer.setType(CustomerEnum.FRECUENT);
            customerService.saveCustomer(customer);
        }

        System.out.println("El cliente " + customer.getName() + " ha sido notificado sobre el pedido con el id " + order.getId());
    }

}