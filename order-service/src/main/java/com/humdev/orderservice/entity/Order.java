package com.humdev.orderservice.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "order_time")
    private LocalDateTime orderTime;

    @Column(name = "order_number")
    private LocalDateTime orderNumber;


}
