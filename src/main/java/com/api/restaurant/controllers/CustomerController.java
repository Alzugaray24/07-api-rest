package com.api.restaurant.controllers;

import com.api.restaurant.dto.customer.CustomerRequestDTO;
import com.api.restaurant.dto.customer.CustomerResponseDTO;
import com.api.restaurant.models.Customer;
import com.api.restaurant.models.Order;
import com.api.restaurant.services.CustomerService;
import com.api.restaurant.proxy.CustomerServiceProxy;
import com.api.restaurant.services.interfaces.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        Customer customer = new Customer(customerRequest.getName(), customerRequest.getEmail());
        customer.setType(customerRequest.getType());

        if (customerRequest.getActive() != null) {
            customer.setActive(customerRequest.getActive());
        }

        Customer savedCustomer = customerService.saveCustomer(customer);
        CustomerResponseDTO response = new CustomerResponseDTO(savedCustomer);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable Long id) {
        try {
            Customer customer = customerService.getCustomerById(id);
            CustomerResponseDTO response = new CustomerResponseDTO(customer);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers(
            @RequestParam(required = false, defaultValue = "false") boolean activeOnly) {

        List<Customer> customers;
        if (activeOnly) {
            customers = customerService.getActiveCustomers();
        } else {
            customers = customerService.getAllCustomers();
        }

        // Asegurar que cada cliente tenga sus órdenes inicializadas antes de convertir
        // a DTO
        customers.forEach(customer -> {
            if (customer.getOrders() != null) {
                // Forzar la inicialización de las órdenes
                customer.getOrders().size();
            }
        });

        List<CustomerResponseDTO> responses = customers.stream()
                .map(customer -> {
                    CustomerResponseDTO dto = new CustomerResponseDTO(customer);
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable Long id,
            @RequestBody CustomerRequestDTO customerRequest) {
        try {
            Customer existingCustomer = customerService.getCustomerById(id);
            existingCustomer.setName(customerRequest.getName());
            existingCustomer.setEmail(customerRequest.getEmail());
            existingCustomer.setType(customerRequest.getType());

            if (customerRequest.getActive() != null) {
                existingCustomer.setActive(customerRequest.getActive());
            }

            Customer updatedCustomer = customerService.updateCustomer(id, existingCustomer);
            CustomerResponseDTO response = new CustomerResponseDTO(updatedCustomer);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<CustomerResponseDTO> updateCustomerStatus(
            @PathVariable Long id,
            @RequestParam boolean active) {
        try {
            Customer customer = customerService.setCustomerStatus(id, active);
            CustomerResponseDTO response = new CustomerResponseDTO(customer);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<CustomerResponseDTO.OrderSummaryDTO>> getCustomerOrders(@PathVariable Long id) {
        try {
            List<Order> orders = customerService.getCustomerOrders(id);
            List<CustomerResponseDTO.OrderSummaryDTO> orderSummaries = orders.stream()
                    .map(CustomerResponseDTO.OrderSummaryDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(orderSummaries);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        try {
            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}