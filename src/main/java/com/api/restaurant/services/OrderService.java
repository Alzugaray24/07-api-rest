package com.api.restaurant.services;

import com.api.restaurant.models.Order;
import com.api.restaurant.models.OrderItem;
import com.api.restaurant.observer.CustomerObserver;
import com.api.restaurant.observer.DishObserver;
import com.api.restaurant.observer.Observable;
import com.api.restaurant.observer.Observer;
import com.api.restaurant.repositories.OrderRepository;
import com.api.restaurant.services.interfaces.IOrderService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.api.restaurant.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService implements Observable, IOrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    @Getter
    private final List<Observer> observers;
    private final CustomerService customerService;
    private final DishService dishService;

    public OrderService(OrderRepository orderRepository, List<Observer> observers, CustomerService customerService,
            DishService dishService) {
        this.orderRepository = orderRepository;
        this.observers = observers;
        this.customerService = customerService;
        this.dishService = dishService;
    }

    @Override
    public Order saveOrder(Order order) {
        logger.info("Guardando nuevo pedido para cliente ID: {}", order.getCustomer().getId());
        try {
            order.getCustomer().addOrder(order);
            logger.debug("Relación bidireccional establecida entre cliente {} y pedido", order.getCustomer().getName());

            CustomerObserver customerObserver = new CustomerObserver(order.getCustomer(), orderRepository,
                    customerService);
            if (!observers.contains(customerObserver)) {
                addObserver(customerObserver);
                logger.debug("Añadido observador de cliente para ID: {}", order.getCustomer().getId());
            }

            order.getOrderItems().forEach(item -> {
                DishObserver dishObserver = new DishObserver(item.getDish(), orderRepository, dishService);
                if (!observers.contains(dishObserver)) {
                    addObserver(dishObserver);
                    logger.debug("Añadido observador de plato para ID: {}", item.getDish().getId());
                }
            });

            Order savedOrder = orderRepository.save(order);
            logger.info("Pedido guardado con ID: {}, con {} items", savedOrder.getId(),
                    savedOrder.getOrderItems().size());
            notifyObservers();
            logger.debug("Observadores notificados");
            return savedOrder;
        } catch (Exception e) {
            logger.error("Error al guardar pedido: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public Order getOrderById(Long id) {
        logger.debug("Buscando pedido con ID: {}", id);
        try {
            Order order = orderRepository.findById(id).orElse(null);
            if (order == null) {
                logger.info("No se encontró pedido con ID: {}", id);
            } else {
                logger.debug("Pedido encontrado con ID: {}, cliente: {}", id, order.getCustomer().getName());
            }
            return order;
        } catch (Exception e) {
            logger.error("Error al buscar pedido con ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public List<Order> getAllOrders() {
        logger.debug("Obteniendo todos los pedidos");
        try {
            List<Order> orders = orderRepository.findAll();
            logger.info("Se encontraron {} pedidos", orders.size());
            return orders;
        } catch (Exception e) {
            logger.error("Error al obtener todos los pedidos: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteOrder(Long id) {
        logger.info("Eliminando pedido con ID: {}", id);
        try {
            // Obtener el pedido antes de eliminarlo
            Order order = orderRepository.findById(id).orElse(null);
            if (order != null) {
                // Eliminar la referencia en el cliente
                order.getCustomer().removeOrder(order);
                logger.debug("Eliminada referencia del pedido en el cliente {}", order.getCustomer().getName());
            }

            orderRepository.deleteById(id);
            logger.info("Pedido con ID {} eliminado correctamente", id);
        } catch (Exception e) {
            logger.error("Error al eliminar pedido con ID {}: {}", id, e.getMessage());
            throw e;
        }
    }

    @Override
    public Order updateOrder(Long id, Order updatedOrder) {
        if (orderRepository.existsById(id)) {
            logger.debug("Updating order with id: {}", id);
            updatedOrder.setId(id);
            return orderRepository.save(updatedOrder);
        }
        logger.error("Order with id {} not found for update", id);
        throw new ResourceNotFoundException("Order with ID " + id + " not found");
    }

    @Override
    public Order setOrderStatus(Long id, boolean active) {
        Order order = getOrderById(id);
        logger.info("Changing active status of order with id: {} to: {}", id, active);
        order.setActive(active);
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getActiveOrders() {
        logger.info("Retrieving all active orders");
        return orderRepository.findAll().stream()
                .filter(Order::isActive)
                .collect(Collectors.toList());
    }

    @Override
    public void addObserver(Observer observer) {
        logger.debug("Añadiendo nuevo observador de tipo: {}", observer.getClass().getSimpleName());
        observers.add(observer);
    }

    @Override
    public void notifyObservers() {
        logger.debug("Notificando a {} observadores", observers.size());
        List<Order> orders = orderRepository.findAll();
        if (!orders.isEmpty()) {
            Order latestOrder = orders.get(orders.size() - 1);
            logger.debug("Notificando sobre el pedido más reciente, ID: {}", latestOrder.getId());
            observers.forEach(observer -> observer.update(latestOrder));
        } else {
            logger.warn("No hay pedidos para notificar a los observadores");
        }
    }
}