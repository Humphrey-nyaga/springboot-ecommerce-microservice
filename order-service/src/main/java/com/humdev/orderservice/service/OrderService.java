package com.humdev.orderservice.service;

import java.time.LocalDate;
import java.util.List;

import com.humdev.orderservice.model.OrderRequest;
import com.humdev.orderservice.model.OrderResponse;

public interface OrderService {
    String createOrder(OrderRequest orderRequest);

    List<OrderResponse> getAllOrders();

    List<OrderResponse> getOrdersByDateRange(LocalDate startDate, LocalDate endDate);

}
