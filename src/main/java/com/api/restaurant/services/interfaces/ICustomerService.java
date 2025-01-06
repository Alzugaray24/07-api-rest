package com.api.restaurant.services.interfaces;

import com.api.restaurant.models.Customer;
import java.util.List;

public interface ICustomerService {
    Customer saveCustomer(Customer customer);
    Customer getCustomerById(Long id);
    void deleteCustomer(Long id);
    void updateCustomer(Long id, Customer updatedCustomer);
    List<Customer> getAllCustomers();
}