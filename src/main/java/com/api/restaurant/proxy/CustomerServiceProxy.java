package com.api.restaurant.proxy;

import com.api.restaurant.models.Customer;
import com.api.restaurant.services.interfaces.ICustomerService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerServiceProxy implements ICustomerService {
    private final ICustomerService customerService;
    private final Map<Long, Customer> customerCache = new HashMap<>();
    private final Map<Long, List<Customer>> allCustomersCache = new HashMap<>();

    public CustomerServiceProxy(ICustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        Customer savedCustomer = customerService.saveCustomer(customer);
        customerCache.put(savedCustomer.getId(), savedCustomer);
        allCustomersCache.clear();
        return savedCustomer;
    }

    @Override
    public Customer getCustomerById(Long id) {
        if (customerCache.containsKey(id)) {
            return customerCache.get(id);
        }
        Customer customer = customerService.getCustomerById(id);
        if (customer != null) {
            customerCache.put(id, customer);
        }
        return customer;
    }

    @Override
    public void deleteCustomer(Long id) {
        customerService.deleteCustomer(id);
        customerCache.remove(id);
        allCustomersCache.clear();
    }

    @Override
    public void updateCustomer(Long id, Customer updatedCustomer) {
        customerService.updateCustomer(id, updatedCustomer);
        customerCache.put(id, updatedCustomer);
        allCustomersCache.clear();
    }

    @Override
    public List<Customer> getAllCustomers() {
        if (allCustomersCache.containsKey(0L)) {
            return allCustomersCache.get(0L);
        }
        List<Customer> customers = customerService.getAllCustomers();
        allCustomersCache.put(0L, customers);
        return customers;
    }
}