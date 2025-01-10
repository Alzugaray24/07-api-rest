package com.api.restaurant.services;

import com.api.restaurant.models.Customer;
import com.api.restaurant.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CustomerServiceTest {

    private CustomerService customerService;
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        customerService = new CustomerService(customerRepository);
    }

    @Test
    @DisplayName("Guardar Cliente")
    void testSaveCustomer() {
        Customer customer = new Customer();
        customer.setName("John Doe");

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer savedCustomer = customerService.saveCustomer(customer);

        assertEquals("John Doe", savedCustomer.getName());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Obtener Cliente por ID")
    void testGetCustomerById() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));

        Customer foundCustomer = customerService.getCustomerById(1L);

        assertEquals(1L, foundCustomer.getId());
        assertEquals("John Doe", foundCustomer.getName());
        verify(customerRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Obtener Cliente por ID - No Encontrado")
    void testGetCustomerByIdNotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        Customer foundCustomer = customerService.getCustomerById(1L);

        assertEquals(null, foundCustomer);
        verify(customerRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Obtener Todos los Clientes")
    void testGetAllCustomers() {
        List<Customer> customers = List.of(
                new Customer("John Doe"),
                new Customer("Jane Doe"),
                new Customer("Jim Beam")
        );

        when(customerRepository.findAll()).thenReturn(customers);

        List<Customer> foundCustomers = customerService.getAllCustomers();

        assertEquals(3, foundCustomers.size());
        assertEquals("John Doe", foundCustomers.get(0).getName());
        assertEquals("Jane Doe", foundCustomers.get(1).getName());
        assertEquals("Jim Beam", foundCustomers.get(2).getName());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Actualizar Cliente")
    void testUpdateCustomer() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setName("John Doe");

        Customer updatedCustomer = new Customer();
        updatedCustomer.setName("John Doe Updated");

        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        Customer result = customerService.updateCustomer(1L, updatedCustomer);

        assertEquals("John Doe Updated", result.getName());
        verify(customerRepository, times(1)).findById(anyLong());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    @DisplayName("Actualizar Cliente - No Encontrado")
    void testUpdateCustomerNotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        Customer updatedCustomer = new Customer();
        updatedCustomer.setName("John Doe Updated");

        assertThrows(RuntimeException.class, () -> customerService.updateCustomer(1L, updatedCustomer));
        verify(customerRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Eliminar Cliente")
    void testDeleteCustomer() {
        doNothing().when(customerRepository).deleteById(anyLong());

        customerService.deleteCustomer(1L);

        verify(customerRepository, times(1)).deleteById(anyLong());
    }
}