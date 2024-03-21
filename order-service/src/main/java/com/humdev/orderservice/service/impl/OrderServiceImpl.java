package com.humdev.orderservice.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.humdev.orderservice.config.GenerateOrderID;
import com.humdev.orderservice.entity.Order;
import com.humdev.orderservice.entity.OrderItem;
import com.humdev.orderservice.entity.OrderItemResponse;
import com.humdev.orderservice.model.ApiResponse;
import com.humdev.orderservice.model.OrderItemRequest;
import com.humdev.orderservice.model.OrderRequest;
import com.humdev.orderservice.repository.OrderRepository;
import com.humdev.orderservice.service.OrderService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final GenerateOrderID generateOrderID;
    private final WebClient webClient;

    public OrderServiceImpl(OrderRepository orderRepository, GenerateOrderID generateOrderID, WebClient webClient) {
        this.orderRepository = orderRepository;
        this.generateOrderID = generateOrderID;
        this.webClient = webClient;
    }

    public Order createOrder(OrderRequest orderRequest) {

        List<String> productCodes = new ArrayList();
        List<Integer> productQuantity = new ArrayList();

        for (OrderItemRequest orderItemRequest : orderRequest.getOrderItems()) {

            productCodes.add(orderItemRequest.getProductCode());
            productQuantity.add(orderItemRequest.getQuantity());
        }

        log.info("Product Codes " + productCodes);
        log.info("Product Quantities " + productQuantity);
        ApiResponse<Boolean> response = this.checkItemsAvailabilityInInventory(productCodes, productQuantity);

        if (response.isSuccess()) {
            Order order = new Order();

            order.setOrderNumber(generateOrderID.generateOrderUUIDString());

            var orderItems = orderRequest.getOrderItems().stream().map(this::mapToOrderItemEntity).toList();
            order.setOrderItems(orderItems);

            var newOrder = orderRepository.save(order);
            return newOrder;

        } else {

            log.error("Not Enough stock :::::::::::: " + response.getData());

            return null;

        }

    }

    private ApiResponse<Boolean> checkItemsAvailabilityInInventory(List<String> productCodes,
            List<Integer> productQuantity) {
        ApiResponse<Boolean> response = webClient.get()
                .uri("http://localhost:6002/api/v1/inventory/validateInventory",
                        uriBuilder -> uriBuilder
                                .queryParam("productQuantities", productQuantity)
                                .queryParam("productCodes", productCodes)
                                .build())

                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> {})
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<Boolean>>() {
                })
                .block();
        log.info("::::::::::::Returning inventory check response:::::::::::::::::::::::");
        return response;
    }

    private OrderItem mapToOrderItemEntity(OrderItemRequest orderItemRequest) {
        OrderItem orderItem = new OrderItem();
        BeanUtils.copyProperties(orderItemRequest, orderItem);
        return orderItem;
    }

    private OrderItemResponse mapToOrderItemResponse(OrderItem orderItem) {
        OrderItemResponse orderItemResponse = new OrderItemResponse();
        BeanUtils.copyProperties(orderItem, orderItemResponse);
        return orderItemResponse;
    }

}
