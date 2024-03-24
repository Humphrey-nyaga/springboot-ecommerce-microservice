package com.humdev.inventoryservice.service;

import java.util.List;

import com.humdev.inventoryservice.model.InventoryRequest;
import com.humdev.inventoryservice.model.InventoryResponse;

public interface InventoryService {

    InventoryResponse createInventory(InventoryRequest inventoryRequest);

    List<InventoryResponse> findAllInventory();

    Boolean checkInventory(List<String> productCodes, List<Integer> productQuantities);
    Boolean reduceInventory(List<String> productCodes, List<Integer> productQuantities);
    void deleteInventoryById(String inventoryId);
}
