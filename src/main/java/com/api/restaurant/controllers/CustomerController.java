package com.api.restaurant.controllers;

import com.api.restaurant.models.Customer;
import com.api.restaurant.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    private final CustomerService service;

    @Autowired
    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<String> saveCustomer(@RequestBody Customer customerRequest) {
        Customer customer = new Customer(
                customerRequest.getName()
        );
        service.saveCustomer(customer);
        return ResponseEntity.ok("El cliente se ha guardado correctamente");
    }

}
