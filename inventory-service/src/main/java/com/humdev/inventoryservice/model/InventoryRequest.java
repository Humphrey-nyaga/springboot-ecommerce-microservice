package com.humdev.inventoryservice.model;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryRequest {

    
    private String productCode;
    
    @Min(value = 0, message = "Quantity cannot be less than zero")
    private int quantity;
}