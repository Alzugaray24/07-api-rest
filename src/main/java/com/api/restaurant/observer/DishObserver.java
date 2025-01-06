package com.api.restaurant.observer;

import com.api.restaurant.models.Dish;
import com.api.restaurant.models.DishEnum;
import com.api.restaurant.models.Order;
import com.api.restaurant.repositories.OrderRepository;
import com.api.restaurant.services.DishService;
import lombok.Getter;

public class DishObserver implements Observer {

    @Getter
    private final Dish dish;
    private final OrderRepository orderRepository;
    private final DishService dishService;

    public DishObserver(Dish dish, OrderRepository orderRepository, DishService dishService) {
        this.dish = dish;
        this.orderRepository = orderRepository;
        this.dishService = dishService;
    }

    @Override
    public void update(Order order) {
        long dishCount = orderRepository.countByDishes_Id(dish.getId());
        System.out.println("cantidad de platos vendidos" + dishCount);
        if (dishCount > 100) {
            dish.setType(DishEnum.POPULAR);
            dishService.saveDish(dish);
        }

        if (DishEnum.POPULAR.equals(dish.getType())) {
            double increment = dish.getPrice() * 0.0573;
            dish.setPrice(dish.getPrice() + increment);
        }

        System.out.println("El plato " + dish.getName() + " ha sido actualizado");
    }
}