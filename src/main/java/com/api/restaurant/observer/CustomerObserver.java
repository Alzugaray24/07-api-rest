package com.api.restaurant.observer;

import com.api.restaurant.models.Customer;
import com.api.restaurant.models.CustomerEnum;
import com.api.restaurant.models.Dish;
import com.api.restaurant.models.Order;
import com.api.restaurant.repositories.OrderRepository;
import com.api.restaurant.services.CustomerService;
import lombok.Getter;

public class CustomerObserver implements Observer {

    @Getter
    private final Customer customer;
    private final OrderRepository orderRepository;
    private final CustomerService customerService;

    public CustomerObserver(Customer customer, OrderRepository orderRepository, CustomerService customerService) {
        this.customer = customer;
        this.orderRepository = orderRepository;
        this.customerService = customerService;
    }

    @Override
    public void update(Order order) {
        long orderCount = orderRepository.countByCustomerId(customer.getId());
        if (orderCount >= 10) {
            customer.setType(CustomerEnum.FRECUENT);

            customerService.saveCustomer(customer);
        }

        if (CustomerEnum.FRECUENT.equals(customer.getType())) {
            double discount = 0.0238;
            double dishesPrice = order.getDishes().stream().map(Dish::getPrice).reduce(0.0, Double::sum);
            double total = dishesPrice - (dishesPrice * discount);
            order.setTotal(total);
            System.out.println("El precio se actualizo a " + total + " para el cliente " + customer.getName() + "por ser frecuente");
        }

        System.out.println("El cliente " + customer.getName() + " ha sido notificado sobre el pedido con el id " + order.getId());
    }

}