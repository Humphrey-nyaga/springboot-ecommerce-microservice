package com.humdev.orderservice.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humdev.orderservice.entity.Order;
import com.humdev.orderservice.model.ApiResponse;
import com.humdev.orderservice.model.OrderItemRequest;
import com.humdev.orderservice.model.OrderRequest;
import com.humdev.orderservice.service.OrderService;

@RestController
@RequestMapping("api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/")
    public ApiResponse<Order> placeOrder(@RequestBody OrderRequest orderRequest) {
        Order order = orderService.createOrder(orderRequest);
        ApiResponse<Order> response = ApiResponse.<Order>builder()
                .message("Order placed successfully")
                .success(true)
                .itemCount(1)
                .data(order)
                .build();
        return response;
    }
}
