package com.api.restaurant.controllers;

import com.api.restaurant.dto.customer.CustomerRequestDTO;
import com.api.restaurant.dto.customer.CustomerResponseDTO;
import com.api.restaurant.models.Customer;
import com.api.restaurant.models.CustomerEnum;
import com.api.restaurant.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CustomerControllerTest {

    private WebTestClient webTestClient;
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = mock(CustomerService.class);
        webTestClient = WebTestClient.bindToController(new CustomerController(customerService)).build();
    }

    @Test
    @DisplayName("Save Customer")
    void testSaveCustomer() {
        CustomerRequestDTO requestDTO = new CustomerRequestDTO();
        requestDTO.setName("John Doe");

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setType(CustomerEnum.NORMAL);

        when(customerService.saveCustomer(any(Customer.class))).thenReturn(customer);

        webTestClient
                .post()
                .uri("/api/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CustomerResponseDTO.class)
                .value(response -> {
                    assertEquals(customer.getId(), response.getId());
                    assertEquals(customer.getName(), response.getName());
                    assertEquals(customer.getType(), response.getType());
                });

        verify(customerService).saveCustomer(any(Customer.class));
    }

    @Test
    @DisplayName("Get Customer by ID")
    void testGetCustomerById() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");
        customer.setType(CustomerEnum.NORMAL);

        when(customerService.getCustomerById(anyLong())).thenReturn(customer);

        webTestClient
                .get()
                .uri("/api/customer/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CustomerResponseDTO.class)
                .value(response -> {
                    assertEquals(customer.getId(), response.getId());
                    assertEquals(customer.getName(), response.getName());
                    assertEquals(customer.getType(), response.getType());
                });

        verify(customerService).getCustomerById(anyLong());
    }

    @Test
    @DisplayName("Get All Customers")
    void testGetAllCustomers() {
        List<Customer> customers = List.of(
                new Customer("John Doe"),
                new Customer("Jane Doe"),
                new Customer("Jim Beam")
        );

        when(customerService.getAllCustomers()).thenReturn(customers);

        webTestClient
                .get()
                .uri("/api/customer")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CustomerResponseDTO.class)
                .hasSize(3)
                .value(response -> {
                    assertEquals("John Doe", response.get(0).getName());
                    assertEquals("Jane Doe", response.get(1).getName());
                    assertEquals("Jim Beam", response.get(2).getName());
                });

        verify(customerService).getAllCustomers();
    }

    @Test
    @DisplayName("Update Customer")
    void testUpdateCustomer() {
        CustomerRequestDTO requestDTO = new CustomerRequestDTO();
        requestDTO.setName("John Doe Updated");

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe Updated");

        when(customerService.updateCustomer(anyLong(), any(Customer.class))).thenReturn(customer);

        webTestClient
                .put()
                .uri("/api/customer/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(CustomerResponseDTO.class)
                .value(response -> {
                    assertEquals(customer.getId(), response.getId());
                    assertEquals(customer.getName(), response.getName());
                    assertEquals(customer.getType(), response.getType());
                });

        verify(customerService).updateCustomer(anyLong(), any(Customer.class));
    }

    @Test
    @DisplayName("Delete Customer")
    void testDeleteCustomer() {
        doNothing().when(customerService).deleteCustomer(anyLong());

        webTestClient
                .delete()
                .uri("/api/customer/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();

        verify(customerService).deleteCustomer(anyLong());
    }
}