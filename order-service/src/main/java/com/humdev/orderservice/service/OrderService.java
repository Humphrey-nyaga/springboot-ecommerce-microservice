package com.humdev.orderservice.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.humdev.orderservice.model.NewOrderResponse;
import com.humdev.orderservice.model.OrderRequest;
import com.humdev.orderservice.model.OrderResponse;

public interface OrderService {
    NewOrderResponse createOrder(OrderRequest orderRequest);

    List<OrderResponse> getAllOrders();

    OrderResponse getOrderByOrderNumber(String orderNumber);

    List<OrderResponse> getOrdersByDateRange(LocalDate startDate, LocalDate endDate);

    Boolean validateOrder(String orderId, BigDecimal orderAmount);

}
