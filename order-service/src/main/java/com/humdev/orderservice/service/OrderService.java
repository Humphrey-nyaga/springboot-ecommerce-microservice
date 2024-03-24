package com.humdev.orderservice.service;

import java.util.List;

import com.humdev.orderservice.model.OrderRequest;
import com.humdev.orderservice.model.OrderResponse;

public interface OrderService {
    String createOrder(OrderRequest orderRequest);
    List<OrderResponse> getAllOrders();
}
