package com.humdev.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humdev.orderservice.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
}
