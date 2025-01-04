package com.api.restaurant.services;

import com.api.restaurant.models.Customer;
import com.api.restaurant.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    public void updateCustomer(Long id, Customer customer) {
        customerRepository.findById(id).map(x -> {
            x.setName(customer.getName());
            x.setType(customer.getType());
            return customerRepository.save(x);
        }).orElseThrow(() -> new RuntimeException("El cliente con el id " + id + " no se ha encontrado"));
    }
}
