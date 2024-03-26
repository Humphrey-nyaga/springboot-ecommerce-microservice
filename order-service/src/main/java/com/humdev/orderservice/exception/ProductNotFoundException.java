package com.humdev.orderservice.exception;

import java.util.Set;

public class ProductNotFoundException extends RuntimeException {

    private Set<String> unavailableProducts;

    public ProductNotFoundException(String message, Set<String> unavailableProducts) {
        super(message);
        this.unavailableProducts = unavailableProducts;
    }

    public Set<String> getUnavailableProducts() {
        return unavailableProducts;
    }
}
