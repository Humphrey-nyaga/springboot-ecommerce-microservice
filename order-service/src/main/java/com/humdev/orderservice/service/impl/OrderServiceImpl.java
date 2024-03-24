package com.humdev.orderservice.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.humdev.orderservice.config.GenerateOrderID;
import com.humdev.orderservice.entity.Order;
import com.humdev.orderservice.entity.OrderItem;
import com.humdev.orderservice.entity.OrderItemResponse;
import com.humdev.orderservice.exception.InvalidStartAndEndDatesException;
import com.humdev.orderservice.exception.InventoryServiceException;
import com.humdev.orderservice.exception.MissingDateRangeException;
import com.humdev.orderservice.exception.NotEnoughQuantityException;
import com.humdev.orderservice.exception.OrderServiceException;
import com.humdev.orderservice.model.ApiResponse;
import com.humdev.orderservice.model.OrderItemRequest;
import com.humdev.orderservice.model.OrderRequest;
import com.humdev.orderservice.model.OrderResponse;
import com.humdev.orderservice.repository.OrderRepository;
import com.humdev.orderservice.service.OrderService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final GenerateOrderID generateOrderID;
    private final WebClient.Builder webClient;

    public OrderServiceImpl(OrderRepository orderRepository, GenerateOrderID generateOrderID,
            WebClient.Builder webClient) {
        this.orderRepository = orderRepository;
        this.generateOrderID = generateOrderID;
        this.webClient = webClient;
    }

    public String createOrder(OrderRequest orderRequest) {

        List<String> productCodes = new ArrayList();
        List<Integer> productQuantity = new ArrayList();

        for (OrderItemRequest orderItemRequest : orderRequest.getOrderItems()) {

            productCodes.add(orderItemRequest.getProductCode());
            productQuantity.add(orderItemRequest.getQuantity());
        }

        log.info("Product Codes " + productCodes);
        log.info("Product Quantities " + productQuantity);
        ApiResponse<?> response = this.checkItemsAvailabilityInInventory(productCodes, productQuantity);

        if (response.isSuccess()) {
            Order order = new Order();

            order.setOrderNumber(generateOrderID.generateOrderUUIDString());

            var orderItems = orderRequest.getOrderItems().stream().map(this::mapToOrderItemEntity).toList();
            order.setOrderItems(orderItems);

            var newOrder = orderRepository.save(order);

            // reduce the inventory here
            ApiResponse<?> inventoryReduced = this.reduceItemsInventory(productCodes, productQuantity);

            return newOrder.getOrderNumber();

        } else {

            log.error("Not Enough stock >>>>>>>>> :::::::::::: " + response.getData());

            if (response.getData() instanceof Map) {
                throw new NotEnoughQuantityException("Not enough quantity in stock",
                        (Map<String, Integer>) response.getData());
            }
            throw new OrderServiceException(response.getMessage());

        }

    }

    private ApiResponse<?> reduceItemsInventory(List<String> productCodes, List<Integer> productQuantity) {
        ApiResponse<?> response = webClient.build().get()
                .uri("http://INVENTORY-SERVICE/api/v1/inventory/reduceInventory",
                        uriBuilder -> uriBuilder
                                .queryParam("productQuantities", productQuantity)
                                .queryParam("productCodes", productCodes)
                                .build())

                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<?>>() {
                })
                .block();
        log.info("::::::::::::Reducing inventory response:::::::::::::::::::::::");
        return response;
    }

    private ApiResponse<?> checkItemsAvailabilityInInventory(List<String> productCodes,
            List<Integer> productQuantity) {
        ApiResponse<?> response = webClient.build().get()
                .uri("http://INVENTORY-SERVICE/api/v1/inventory/validateInventory",
                        uriBuilder -> uriBuilder
                                .queryParam("productQuantities", productQuantity)
                                .queryParam("productCodes", productCodes)
                                .build())

                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> handleError(clientResponse))
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<?>>() {
                })
                .block();
        log.info("::::::::::::Returning inventory check response:::::::::::::::::::::::");
        return response;
    }

    private Mono<? extends Throwable> handleError(ClientResponse clientResponse) {
        log.error("Error encountered: {} ", clientResponse.statusCode());
        return Mono.error(new InventoryServiceException("Error in the inventory service"));
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

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse orderResponse = new OrderResponse();
        BeanUtils.copyProperties(order, orderResponse);
        return orderResponse;
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(this::mapToOrderResponse).toList();
    }


    @Override
    public List<OrderResponse> getOrdersByDateRange(LocalDate startDate, LocalDate endDate) {

        if (startDate == null && endDate == null) {
            throw new MissingDateRangeException("At least one of startDate or endDate must be provided");
        }
        if (startDate != null && endDate != null) {
            return orderRepository.findByOrderTimeBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay())
                    .stream()
                    .map(this::mapToOrderResponse)
                    .toList();

        } else if (startDate != null) {
            return orderRepository.findByOrderTimeOnOrAfter(startDate.atStartOfDay())
                    .stream()
                    .map(this::mapToOrderResponse)
                    .toList();

        } else if (endDate != null) {
            return orderRepository.findByOrderTimeBeforeOrOn(endDate.plusDays(1).atStartOfDay())
                    .stream()
                    .map(this::mapToOrderResponse)
                    .toList();
                    
        } else {
            throw new MissingDateRangeException("At least one of startDate or endDate must be provided");
        }

    }

}
