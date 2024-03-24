package com.humdev.orderservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.GetExchange;

import com.humdev.orderservice.entity.Order;
import com.humdev.orderservice.model.ApiResponse;
import com.humdev.orderservice.model.OrderRequest;
import com.humdev.orderservice.model.OrderResponse;
import com.humdev.orderservice.service.OrderService;

@RestController
@RequestMapping("api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/")
    public ApiResponse<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        String orderNumber = orderService.createOrder(orderRequest);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message("Order placed successfully")
                .success(true)
                .itemCount(1)
                .data(orderNumber)
                .build();
        return response;
    } 

    @GetMapping("/")
    public ApiResponse<List<OrderResponse>> getALlOrders() {
        List<OrderResponse> ordersResponse = orderService.getAllOrders();
        ApiResponse<List<OrderResponse>> response = ApiResponse.<List<OrderResponse>>builder()
                .message("All Orders")
                .success(true)
                .itemCount(ordersResponse.size())
                .data(ordersResponse)
                .build();
        return response;
    }
}
