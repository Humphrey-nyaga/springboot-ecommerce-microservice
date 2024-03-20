package com.humdev.inventoryservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humdev.inventoryservice.entity.Inventory;
import com.humdev.inventoryservice.model.ApiResponse;
import com.humdev.inventoryservice.model.InventoryRequest;
import com.humdev.inventoryservice.model.InventoryResponse;
import com.humdev.inventoryservice.service.InventoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/")
    public ApiResponse<InventoryResponse> createInventory(@RequestBody InventoryRequest inventoryRequest) {

        InventoryResponse newInventory = inventoryService.createInventory(inventoryRequest);
        ApiResponse<InventoryResponse> response = ApiResponse.<InventoryResponse>builder()
                .data(newInventory)
                .message("Inventory added successfully")
                .success(true)
                .build();
        return response;
    }

    @GetMapping("/")
    public ApiResponse<List<InventoryResponse>> findAllInventory() {

        List<InventoryResponse> inventoryList = inventoryService.findAllInventory();
        ApiResponse<List<InventoryResponse>> response = ApiResponse.<List<InventoryResponse>>builder()
                .data(inventoryList)
                .message("Inventory retrieved successfully")
                .itemCount(inventoryList.size())
                .success(true)
                .build();
        return response;
    }
}
