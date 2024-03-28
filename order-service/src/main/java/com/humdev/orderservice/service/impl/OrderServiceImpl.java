package com.humdev.orderservice.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.humdev.orderservice.config.GenerateOrderID;
import com.humdev.orderservice.entity.Order;
import com.humdev.orderservice.entity.OrderItem;
import com.humdev.orderservice.model.OrderItemResponse;
import com.humdev.orderservice.entity.OrderStatus;
import com.humdev.orderservice.exception.InventoryServiceException;
import com.humdev.orderservice.exception.MissingDateRangeException;
import com.humdev.orderservice.exception.NotEnoughQuantityException;
import com.humdev.orderservice.exception.OrderAmountDoesNotMatchException;
import com.humdev.orderservice.exception.OrderNotFoundException;
import com.humdev.orderservice.exception.OrderServiceException;
import com.humdev.orderservice.exception.ProductServiceException;
import com.humdev.orderservice.model.ApiResponse;
import com.humdev.orderservice.model.NewOrderResponse;
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

    public NewOrderResponse createOrder(OrderRequest orderRequest) {

        Order order = new Order();

        List<String> productCodes = new ArrayList();
        List<Integer> productQuantity = new ArrayList();

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest orderItemRequest : orderRequest.getOrderItems()) {

            productCodes.add(orderItemRequest.getProductCode());
            productQuantity.add(orderItemRequest.getQuantity());

            OrderItem orderItem = mapToOrderItemEntity(orderItemRequest);
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

        log.info("Product Codes " + productCodes);
        log.info("Product Quantities " + productQuantity);

        ApiResponse<?> itemAvailabilityResponse = this.checkItemsAvailabilityInInventory(productCodes, productQuantity);
        ApiResponse<?> orderItemsPrices = this.fetchItemPricesFromProductService(productCodes);

        if (itemAvailabilityResponse.isSuccess() && orderItemsPrices.isSuccess()) {

            order.setOrderNumber(generateOrderID.generateOrderUUIDString());

            log.info(":::::::::::::order uuid::::::::::: " + order.getOrderNumber());

            // reduce the inventory first
            ApiResponse<?> inventoryReduced = this.reduceItemsInventory(productCodes, productQuantity);
            log.info("::::::Inventory Reduced ::::::::::: " + order.getOrderNumber());

            // TODO -> We dont want to be having total stored in the db they need to be
            // tabulated from the db instead using a query
            List<Double> orderItemsPricesData = (List<Double>) orderItemsPrices.getData();
            List<BigDecimal> productPrices = orderItemsPricesData.stream()
                    .map(BigDecimal::valueOf)
                    .collect(Collectors.toList());
            BigDecimal orderTotal = this.calculateOrderTotal(productCodes, productQuantity, productPrices);
            order.setOrderTotal(orderTotal);
            order.setOrderStatus(OrderStatus.PENDING);
            order.setOrderItems(orderItems);

            // save
            NewOrderResponse newOrderResponse = this.mapToNewOrderResponse(orderRepository.save(order));

            newOrderResponse.setPaymentUrl("api/v1/payments/process?orderId=" + newOrderResponse.getOrderNumber());

            return newOrderResponse;

        } else {

            log.error("Not Enough stock >>>>>>>>> :::::::::::: " + itemAvailabilityResponse.getData());

            if (itemAvailabilityResponse.getData() instanceof Map) {
                throw new NotEnoughQuantityException("Not enough quantity in stock",
                        (Map<String, Integer>) itemAvailabilityResponse.getData());
            }
            throw new OrderServiceException(itemAvailabilityResponse.getMessage());

        }

    }

    // use this for only new order - a payment url needs to be added.
    // TODO consolidate this with the existing OrderResponse mapper
    private NewOrderResponse mapToNewOrderResponse(Order newOrder) {
        NewOrderResponse orderResponse = new NewOrderResponse();
        BeanUtils.copyProperties(newOrder, orderResponse);
        return orderResponse;

    }

    private ApiResponse<?> reduceItemsInventory(List<String> productCodes, List<Integer> productQuantity) {
        ApiResponse<?> itemAvailabilityResponse = webClient.build().get()
                .uri("http://INVENTORY-SERVICE/api/v1/inventory/reduceInventory",
                        uriBuilder -> uriBuilder
                                .queryParam("productQuantities", productQuantity)
                                .queryParam("productCodes", productCodes)
                                .build())

                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<?>>() {
                })
                .block();
        log.info("::::::::::::Reducing inventory itemAvailabilityResponse:::::::::::::::::::::::");
        return itemAvailabilityResponse;
    }

    private ApiResponse<?> checkItemsAvailabilityInInventory(List<String> productCodes,
            List<Integer> productQuantity) {
        ApiResponse<?> itemAvailabilityResponse = webClient.build().get()
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
        log.info("::::::::::::Returning inventory check itemAvailabilityResponse:::::::::::::::::::::::");
        return itemAvailabilityResponse;
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

    private ApiResponse<?> fetchItemPricesFromProductService(List<String> productCodes) {
        ApiResponse<?> response = webClient.build().get()
                .uri("http://PRODUCT-SERVICE/api/v1/products/productPrices",
                        uriBuilder -> uriBuilder
                                .queryParam("productCodes", productCodes)
                                .build())

                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> Mono
                                .error(new ProductServiceException("An error occurred in the products service")))
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<?>>() {
                })
                .block();
        log.info(":::::::::::::::::Location----->>>> Order Service::::::::::::::::::");
        log.info(":::::::::::::::::Returned Product Prices::::::::::::::::::  " + response.getData());
        return response;
    }

    private BigDecimal calculateOrderTotal(List<String> productCodes, List<Integer> productQuantity,
            List<BigDecimal> productPrices) {

        BigDecimal orderTotal = BigDecimal.ZERO;
        for (int i = 0; i < productCodes.size(); i++) {
            orderTotal = orderTotal.add(productPrices.get(i).multiply(new BigDecimal(productQuantity.get(i))));
        }
        return orderTotal;
    }

    @Override
    public Boolean validateOrder(String orderNumber, BigDecimal orderAmount) {

        OrderResponse order = this.getOrderByOrderNumber(orderNumber);

        if (order.getOrderTotal().compareTo(orderAmount) != 0) {
            throw new OrderAmountDoesNotMatchException(
                    "Order amount is invalid. Expected " + order.getOrderTotal() + " but was given " + orderAmount);
        } else {
            return true;
        }
    }

    @Override
    public OrderResponse getOrderByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber).orElseThrow(
                () -> new OrderNotFoundException("Order " + orderNumber + " does not exists."));
        return this.mapToOrderResponse(order);
    }

    @Override
    public List<OrderItemResponse> getOrderItemsByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber).orElseThrow(
                () -> new OrderNotFoundException("Order " + orderNumber + " does not exist."));
        return order.getOrderItems().stream().map(this::mapToOrderItemResponse).toList();
    }

}
