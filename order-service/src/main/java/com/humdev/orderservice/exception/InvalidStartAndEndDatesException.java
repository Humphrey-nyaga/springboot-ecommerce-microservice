package com.humdev.orderservice.exception;

public class InvalidStartAndEndDatesException extends RuntimeException {
    
    public InvalidStartAndEndDatesException(String message) {
        super(message);
    }
}
