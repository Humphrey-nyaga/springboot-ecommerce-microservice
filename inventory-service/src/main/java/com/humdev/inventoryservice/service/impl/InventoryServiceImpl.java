package com.humdev.inventoryservice.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.humdev.inventoryservice.entity.Inventory;
import com.humdev.inventoryservice.model.InventoryRequest;
import com.humdev.inventoryservice.model.InventoryResponse;
import com.humdev.inventoryservice.repository.InventoryRepository;
import com.humdev.inventoryservice.service.InventoryService;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public InventoryResponse createInventory(InventoryRequest inventoryRequest) {
        Inventory inventory = inventoryRepository.save(this.mapToInventoryRequestEntity(inventoryRequest));
        return this.mapToInventoryResponse(inventory);
    }

    public Inventory mapToInventoryRequestEntity(InventoryRequest inventoryRequest) {
        Inventory inventory = new Inventory();
        BeanUtils.copyProperties(inventoryRequest, inventory);
        return inventory;
    }

    public InventoryResponse mapToInventoryResponse(Inventory inventory) {
        InventoryResponse inventoryResponse = new InventoryResponse();
        BeanUtils.copyProperties(inventory, inventoryResponse);
        return inventoryResponse;
    }

    @Override
    public List<InventoryResponse> findAllInventory() {

        List<InventoryResponse> inventory = inventoryRepository.findAll().stream().map(this::mapToInventoryResponse)
                .toList();
        return inventory;
    }
}
