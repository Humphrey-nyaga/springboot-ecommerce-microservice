package com.humdev.productservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Product not found") without controlleradvice the error logs till gets to the client
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
