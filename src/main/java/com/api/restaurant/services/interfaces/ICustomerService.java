package com.api.restaurant.services.interfaces;

import com.api.restaurant.models.Customer;
import com.api.restaurant.models.Order;
import java.util.List;

public interface ICustomerService {
    Customer saveCustomer(Customer customer);

    Customer getCustomerById(Long id);

    void deleteCustomer(Long id);

    Customer updateCustomer(Long id, Customer updatedCustomer);

    List<Customer> getAllCustomers();

    List<Customer> getActiveCustomers();

    Customer setCustomerStatus(Long id, boolean active);

    List<Order> getCustomerOrders(Long customerId);
}