package com.api.restaurant.proxy;

import com.api.restaurant.models.Customer;
import com.api.restaurant.models.Order;
import com.api.restaurant.services.interfaces.ICustomerService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerServiceProxy implements ICustomerService {
    private final ICustomerService customerService;
    private final Map<Long, Customer> customerCache = new HashMap<>();
    private final Map<Long, List<Customer>> allCustomersCache = new HashMap<>();
    private final Map<String, List<Customer>> activeCustomersCache = new HashMap<>();
    private final Map<Long, List<Order>> customerOrdersCache = new HashMap<>();

    public CustomerServiceProxy(ICustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        Customer savedCustomer = customerService.saveCustomer(customer);
        customerCache.put(savedCustomer.getId(), savedCustomer);
        clearAllCaches();
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
        clearAllCaches();
    }

    @Override
    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        Customer updated = customerService.updateCustomer(id, updatedCustomer);
        customerCache.put(id, updated);
        clearAllCaches();
        return updated;
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

    @Override
    public List<Customer> getActiveCustomers() {
        String cacheKey = "active";
        if (activeCustomersCache.containsKey(cacheKey)) {
            return activeCustomersCache.get(cacheKey);
        }
        List<Customer> activeCustomers = customerService.getActiveCustomers();
        activeCustomersCache.put(cacheKey, activeCustomers);
        return activeCustomers;
    }

    @Override
    public Customer setCustomerStatus(Long id, boolean active) {
        Customer updatedCustomer = customerService.setCustomerStatus(id, active);
        customerCache.put(id, updatedCustomer);
        clearAllCaches();
        return updatedCustomer;
    }

    @Override
    public List<Order> getCustomerOrders(Long customerId) {
        if (customerOrdersCache.containsKey(customerId)) {
            return customerOrdersCache.get(customerId);
        }
        List<Order> orders = customerService.getCustomerOrders(customerId);
        customerOrdersCache.put(customerId, orders);
        return orders;
    }

    // Helper method to clear all list caches
    private void clearAllCaches() {
        allCustomersCache.clear();
        activeCustomersCache.clear();
        customerOrdersCache.clear();
    }
}