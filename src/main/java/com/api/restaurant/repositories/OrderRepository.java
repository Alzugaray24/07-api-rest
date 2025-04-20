package com.api.restaurant.repositories;

import com.api.restaurant.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    long countByCustomerId(Long customerId);

    @Query("SELECT COUNT(o) FROM Order o JOIN o.orderItems oi WHERE oi.dish.id = :dishId")
    long countByDishId(@Param("dishId") Long dishId);
}
