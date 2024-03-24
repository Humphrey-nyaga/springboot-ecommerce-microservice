package com.humdev.inventoryservice.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.humdev.inventoryservice.entity.Inventory;
import com.humdev.inventoryservice.exception.NotEnoughQuantityException;
import com.humdev.inventoryservice.exception.ProductNotFoundException;
import com.humdev.inventoryservice.model.InventoryRequest;
import com.humdev.inventoryservice.model.InventoryResponse;
import com.humdev.inventoryservice.repository.InventoryRepository;
import com.humdev.inventoryservice.service.InventoryService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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

        return inventoryRepository.findAll().stream().map(this::mapToInventoryResponse)
                .toList();
    }

    @Override
    public Boolean checkInventory(List<String> productCodes, List<Integer> productQuantities) {
        Map<String, Integer> unavailableItems = new HashMap<>();

        log.info("::::::::Inventory Product Codes ::::::::::::: " + productCodes);
        log.info("::::::::Inventory Product Quantity::::::::::::: " + productQuantities);

        int listSize = productCodes.size();
        for (int i = 0; i < listSize; i++) {

            Integer productQuantity = productQuantities.get(i);
            String productCode = productCodes.get(i);

            Inventory inventory = inventoryRepository.findByProductCode(productCode);
            if (inventory != null) {
                Integer currentDbQuantity = inventory.getQuantity();

                if (productQuantity > currentDbQuantity) {
                    unavailableItems.put(productCode, productQuantity - currentDbQuantity);
                }
            } else {
                unavailableItems.put(productCode, productQuantity);
            }
        }

        if (unavailableItems.isEmpty()) {
            return true;
        } else {
            log.info(":::::::::Unavailable Items:::::::::::::::");
            unavailableItems.forEach((key, value) -> System.out
                    .println("Product Code: " + key + ":::::::::::: Product Quantity: " + value));

            throw new NotEnoughQuantityException("Products quantity not enough in Stock", unavailableItems);
        }
    }

    @Override
    @Transactional
    public Boolean reduceInventory(List<String> productCodes, List<Integer> productQuantities) {
        List<Inventory> inventories = inventoryRepository.findByProductCodeIn(productCodes);
        Map<String, Inventory> inventoryMap = inventories.stream()
                .collect(Collectors.toMap(Inventory::getProductCode, Function.identity()));

        for (int i = 0; i < productCodes.size(); i++) {
            String productCode = productCodes.get(i);
            Integer quantityToReduce = productQuantities.get(i);

            Inventory inventory = inventoryMap.get(productCode);
            if (inventory != null) {
                int updatedQuantity = inventory.getQuantity() - quantityToReduce;
                if (updatedQuantity >= 0) {
                    inventory.setQuantity(updatedQuantity);
                } else {
                    throw new NotEnoughQuantityException("Insufficient quantity for product with code: " + productCode);
                }
            } else {
                throw new ProductNotFoundException("Product with code " + productCode + " not found in inventory");
            }
        }

        inventoryRepository.saveAll(inventories);
        return true;
    }

}
