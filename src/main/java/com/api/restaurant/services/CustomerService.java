package com.api.restaurant.services;

import com.api.restaurant.models.Customer;
import com.api.restaurant.repositories.CustomerRepository;
import com.api.restaurant.services.interfaces.ICustomerService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CustomerService implements ICustomerService {
    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public void updateCustomer(Long id, Customer updatedCustomer) {
        customerRepository.findById(id).map(customer -> {
            customer.setName(updatedCustomer.getName());
            customer.setType(updatedCustomer.getType());
            return customerRepository.save(customer);
        }).orElseThrow(() -> new RuntimeException("El cliente con el id " + id + " no se ha encontrado"));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}