package com.humdev.orderservice.service.impl;

import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.humdev.orderservice.config.GenerateOrderID;
import com.humdev.orderservice.entity.Order;
import com.humdev.orderservice.entity.OrderItem;
import com.humdev.orderservice.entity.OrderItemResponse;
import com.humdev.orderservice.model.OrderItemRequest;
import com.humdev.orderservice.model.OrderRequest;
import com.humdev.orderservice.repository.OrderRepository;
import com.humdev.orderservice.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final GenerateOrderID generateOrderID;

    public OrderServiceImpl(OrderRepository orderRepository, GenerateOrderID generateOrderID) {
        this.orderRepository = orderRepository;
        this.generateOrderID = generateOrderID;
    }

    public OrderItem mapToOrderItemEntity(OrderItemRequest orderItemRequest) {
        OrderItem orderItem = new OrderItem();
        BeanUtils.copyProperties(orderItemRequest, orderItem);
        return orderItem;
    }

    public OrderItemResponse mapToOrderItemResponse(OrderItem orderItem) {
        OrderItemResponse orderItemResponse = new OrderItemResponse();
        BeanUtils.copyProperties(orderItem, orderItemResponse);
        return orderItemResponse;
    }

    public Order createOrder(OrderRequest orderRequest) {

        Order order = new Order();
        order.setOrderNumber(generateOrderID.generateOrderUUIDString());

        var orderItems = orderRequest.getOrderItems().stream().map(this::mapToOrderItemEntity).toList();
        order.setOrderItems(orderItems);

        var newOrder = orderRepository.save(order);
        return newOrder;
    }
}
