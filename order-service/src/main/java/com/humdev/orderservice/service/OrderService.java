package com.humdev.orderservice.service;

import com.humdev.orderservice.entity.Order;
import com.humdev.orderservice.model.OrderRequest;

public interface OrderService {
    Order createOrder(OrderRequest orderRequest);
}
