package com.humdev.productservice.exception;

import java.util.Set;

// @ResponseStatus(value=HttpStatus.NOT_FOUND, reason="Product not found") without controlleradvice the error logs till gets to the client
public class ProductNotFoundException extends RuntimeException {
    private Set<String> unavailableProducts;

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String message, Set<String> unavailableProducts) {
        super(message);
        this.unavailableProducts = unavailableProducts;
    }

    
    public Set<String> getUnavailableProducts() {
        return unavailableProducts;
    }

}
