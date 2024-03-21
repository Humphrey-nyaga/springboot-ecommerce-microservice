package com.humdev.inventoryservice.exception;

import java.util.Map;

public class NotEnoughQuantityException extends RuntimeException {

    private Map<String, Integer> unavailableItems;

    public NotEnoughQuantityException(String message, Map<String, Integer> unavailableItems) {
        super(message);
        this.unavailableItems = unavailableItems;
    }

    public Map<String, Integer> getUnavailableItems() {
        return unavailableItems;
    }

}
