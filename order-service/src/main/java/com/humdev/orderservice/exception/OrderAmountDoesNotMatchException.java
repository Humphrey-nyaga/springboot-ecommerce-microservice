package com.humdev.orderservice.exception;

public class OrderAmountDoesNotMatchException extends RuntimeException {
   
    public OrderAmountDoesNotMatchException(String message) {
        super(message);
    }

}
