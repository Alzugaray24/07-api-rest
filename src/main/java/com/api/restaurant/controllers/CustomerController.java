package com.api.restaurant.controllers;

import com.api.restaurant.dto.customer.CustomerRequestDTO;
import com.api.restaurant.dto.customer.CustomerResponseDTO;
import com.api.restaurant.models.Customer;
import com.api.restaurant.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService service;

    @Autowired
    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> saveCustomer(@RequestBody CustomerRequestDTO customerRequest) {
        Customer customer = new Customer(customerRequest.getName());
        Customer savedCustomer = service.saveCustomer(customer);
        CustomerResponseDTO response = new CustomerResponseDTO();
        response.setId(savedCustomer.getId());
        response.setName(savedCustomer.getName());
        response.setType(savedCustomer.getType());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long id) {
        Customer customer = service.getCustomerById(id);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        CustomerResponseDTO response = new CustomerResponseDTO();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setType(customer.getType());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        List<Customer> customers = service.getAllCustomers();
        List<CustomerResponseDTO> responses = customers.stream()
                .map(customer -> {
                    CustomerResponseDTO response = new CustomerResponseDTO();
                    response.setId(customer.getId());
                    response.setName(customer.getName());
                    response.setType(customer.getType());
                    return response;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerRequestDTO customerRequest) {
        Customer updatedCustomer = new Customer();
        updatedCustomer.setName(customerRequest.getName());
        service.updateCustomer(id, updatedCustomer);
        CustomerResponseDTO response = new CustomerResponseDTO();
        response.setId(updatedCustomer.getId());
        response.setName(updatedCustomer.getName());
        response.setType(updatedCustomer.getType());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        service.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }
}