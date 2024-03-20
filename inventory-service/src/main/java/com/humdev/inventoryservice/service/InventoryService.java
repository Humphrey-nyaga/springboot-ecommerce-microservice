package com.humdev.inventoryservice.service;

import java.util.List;

import com.humdev.inventoryservice.entity.Inventory;
import com.humdev.inventoryservice.model.InventoryRequest;
import com.humdev.inventoryservice.model.InventoryResponse;

public interface InventoryService {
    InventoryResponse createInventory(InventoryRequest inventoryRequest);
    List<InventoryResponse> findAllInventory();
}
