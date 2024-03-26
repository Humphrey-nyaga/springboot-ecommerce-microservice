package com.humdev.orderservice.exception;

public class MissingDateRangeException extends RuntimeException {

    public MissingDateRangeException(String message) {
        super(message);
    }
}
