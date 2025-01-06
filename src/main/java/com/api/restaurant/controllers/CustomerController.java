package com.api.restaurant.controllers;

import com.api.restaurant.dto.customer.CustomerRequestDTO;
import com.api.restaurant.dto.customer.CustomerResponseDTO;
import com.api.restaurant.models.Customer;
import com.api.restaurant.services.CustomerService;
import com.api.restaurant.proxy.CustomerServiceProxy;
import com.api.restaurant.services.interfaces.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final ICustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = new CustomerServiceProxy(customerService);
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> saveCustomer(@RequestBody CustomerRequestDTO customerRequest) {
        Customer customer = new Customer();
        customer.setName(customerRequest.getName());
        customer.setType(customerRequest.getType());
        Customer savedCustomer = customerService.saveCustomer(customer);
        CustomerResponseDTO response = convertToCustomerResponseDTO(savedCustomer);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        }
        CustomerResponseDTO response = convertToCustomerResponseDTO(customer);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        List<CustomerResponseDTO> responses = customers.stream()
                .map(this::convertToCustomerResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable Long id, @RequestBody CustomerRequestDTO customerRequest) {
        Customer updatedCustomer = new Customer();
        updatedCustomer.setName(customerRequest.getName());
        updatedCustomer.setType(customerRequest.getType());
        customerService.updateCustomer(id, updatedCustomer);
        CustomerResponseDTO response = convertToCustomerResponseDTO(updatedCustomer);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    private CustomerResponseDTO convertToCustomerResponseDTO(Customer customer) {
        CustomerResponseDTO response = new CustomerResponseDTO();
        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setType(customer.getType());
        return response;
    }
}