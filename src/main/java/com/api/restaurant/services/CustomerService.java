package com.api.restaurant.services;

import com.api.restaurant.models.Customer;
import com.api.restaurant.models.Order;
import com.api.restaurant.repositories.CustomerRepository;
import com.api.restaurant.services.interfaces.ICustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService implements ICustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer saveCustomer(Customer customer) {
        logger.info("Guardando nuevo cliente: {}", customer.getName());
        try {
            Customer savedCustomer = customerRepository.save(customer);
            logger.info("Cliente guardado con ID: {}", savedCustomer.getId());
            return savedCustomer;
        } catch (Exception e) {
            logger.error("Error al guardar cliente {}: {}", customer.getName(), e.getMessage());
            throw e;
        }
    }

    @Override
    public Customer getCustomerById(Long id) {
        logger.debug("Buscando cliente con ID: {}", id);
        try {
            Customer customer = customerRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Cliente no encontrado con ID: {}", id);
                        return new RuntimeException("Cliente no encontrado con id: " + id);
                    });
            logger.debug("Cliente encontrado: {}", customer.getName());
            return customer;
        } catch (Exception e) {
            logger.error("Error al buscar cliente con ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteCustomer(Long id) {
        logger.info("Eliminando cliente con ID: {}", id);
        try {
            // Verificar que el cliente existe
            Customer customer = getCustomerById(id);
            customerRepository.deleteById(id);
            logger.info("Cliente con ID {} eliminado correctamente", id);
        } catch (Exception e) {
            logger.error("Error al eliminar cliente con ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public Customer updateCustomer(Long id, Customer updatedCustomer) {
        logger.info("Actualizando cliente con ID: {}", id);
        try {
            return customerRepository.findById(id).map(customer -> {
                // Solo actualizamos los campos básicos, manteniendo las relaciones
                customer.setName(updatedCustomer.getName());
                customer.setEmail(updatedCustomer.getEmail());
                customer.setType(updatedCustomer.getType());
                customer.setActive(updatedCustomer.isActive());
                // Las órdenes se mantienen y no se modifican aquí
                logger.debug("Cliente {} actualizado", customer.getName());
                return customerRepository.save(customer);
            }).orElseThrow(() -> {
                logger.warn("No se encontró el cliente con ID {} para actualizar", id);
                return new RuntimeException("El cliente con el id " + id + " no se ha encontrado");
            });
        } catch (Exception e) {
            logger.error("Error al actualizar cliente con ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        logger.debug("Obteniendo todos los clientes");
        try {
            List<Customer> customers = customerRepository.findAll();
            logger.info("Se encontraron {} clientes", customers.size());
            return customers;
        } catch (Exception e) {
            logger.error("Error al obtener todos los clientes: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Customer> getActiveCustomers() {
        logger.debug("Obteniendo clientes activos");
        try {
            List<Customer> activeCustomers = customerRepository.findAll().stream()
                    .filter(Customer::isActive)
                    .collect(Collectors.toList());
            logger.info("Se encontraron {} clientes activos", activeCustomers.size());
            return activeCustomers;
        } catch (Exception e) {
            logger.error("Error al obtener clientes activos: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Customer setCustomerStatus(Long id, boolean active) {
        logger.info("Cambiando estado del cliente con ID {}: activo={}", id, active);
        try {
            return customerRepository.findById(id).map(customer -> {
                customer.setActive(active);
                logger.debug("Estado del cliente {} actualizado a: {}", customer.getName(), active);
                return customerRepository.save(customer);
            }).orElseThrow(() -> {
                logger.warn("No se encontró el cliente con ID {} para actualizar su estado", id);
                return new RuntimeException("El cliente con el id " + id + " no se ha encontrado");
            });
        } catch (Exception e) {
            logger.error("Error al cambiar estado del cliente con ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Order> getCustomerOrders(Long customerId) {
        logger.debug("Obteniendo pedidos del cliente con ID: {}", customerId);
        try {
            Customer customer = getCustomerById(customerId);
            List<Order> orders = customer.getOrders() != null ? customer.getOrders() : Collections.emptyList();
            logger.info("Cliente con ID {} tiene {} pedidos", customerId, orders.size());
            return orders;
        } catch (Exception e) {
            logger.error("Error al obtener pedidos del cliente con ID {}: {}", customerId, e.getMessage());
            throw e;
        }
    }
}