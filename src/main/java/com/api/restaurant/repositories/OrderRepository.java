package com.api.restaurant.repositories;

import com.api.restaurant.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    long countByCustomerId(Long customerId);
    long countByDishes_Id(Long dishId);
}
