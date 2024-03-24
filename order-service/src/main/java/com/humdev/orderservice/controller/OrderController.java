package com.humdev.orderservice.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.humdev.orderservice.model.ApiResponse;
import com.humdev.orderservice.model.OrderRequest;
import com.humdev.orderservice.model.OrderResponse;
import com.humdev.orderservice.service.OrderService;

import jakarta.ws.rs.QueryParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/")
    public ApiResponse<String> placeOrder(@RequestBody OrderRequest orderRequest) {

        long startTime = System.currentTimeMillis();
        log.info("::::Order request Start Time::::: {} " + startTime);
        String orderNumber = orderService.createOrder(orderRequest);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .message("Order placed successfully")
                .success(true)
                .itemCount(1)
                .data(orderNumber)
                .build();

        long stopTime = System.currentTimeMillis();
        log.info("::::Order Stop Time::::: {} " + stopTime);
        log.info("::::Order duration--> request to response Time::::: {} " + (stopTime - startTime));
        return response;

    }

    @GetMapping("/")
    public ApiResponse<List<OrderResponse>> getAllOrders() {

        List<OrderResponse> ordersResponse = new ArrayList<>();

        ordersResponse = orderService.getAllOrders();

        ApiResponse<List<OrderResponse>> response = ApiResponse.<List<OrderResponse>>builder()
                .message("All Orders")
                .success(true)
                .itemCount(ordersResponse.size())
                .data(ordersResponse)
                .build();
        return response;
    }

    @GetMapping("/filter")
    public ApiResponse<List<OrderResponse>> filterOrders(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        List<OrderResponse> ordersResponse = orderService.getOrdersByDateRange(startDate, endDate);
       log.info(":::::::::::Start Date::::::::::: " + startDate);
       log.info(":::::::::::End Date::::::::::: " + endDate);
        ApiResponse<List<OrderResponse>> response = ApiResponse.<List<OrderResponse>>builder()
                .message("All Orders")
                .success(true)
                .itemCount(ordersResponse.size())
                .data(ordersResponse)
                .build();
        return response;
    }

}
