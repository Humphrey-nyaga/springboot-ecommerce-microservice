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

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("api/v1/inventory")
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/")
    @ResponseStatus(code = HttpStatus.CREATED)
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
    @ResponseStatus(code = HttpStatus.OK)
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

    @GetMapping("/validateInventory")
    @ResponseStatus(code = HttpStatus.OK)
    public ApiResponse<Boolean> confirmOrderItemsAvailability(
            @RequestParam("productCodes") List<String> productCodes,
            @RequestParam("productQuantities") List<Integer> productQuantities) {

        log.info("::::::::::::Inventory Controller Called:::::::::::::::::::::::");

        Boolean isValid = inventoryService.checkInventory(productCodes, productQuantities);
        ApiResponse<Boolean> response = ApiResponse.<Boolean>builder()
                .data(isValid)
                .message("Inventory exists")
                .success(true)
                .build();
        return response;
    }
}
