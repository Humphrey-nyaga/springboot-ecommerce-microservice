package com.humdev.orderservice.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humdev.orderservice.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o " +
            "FROM Order o " +
            "WHERE o.orderTime >= :startDate AND o.orderTime < :endDate")
    List<Order> findByOrderTimeBetween(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    @Query("SELECT o " +
            "FROM Order o " +
            "WHERE o.orderTime >= :startDate")
    List<Order> findByOrderTimeOnOrAfter(@Param("startDate") LocalDateTime startDate);


    @Query("SELECT o " +
    "FROM Order o " +
    "WHERE o.orderTime <= :endDate")
    List<Order> findByOrderTimeBeforeOrOn(@Param("endDate")LocalDateTime endDate);

}
