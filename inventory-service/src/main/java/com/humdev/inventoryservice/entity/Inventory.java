package com.humdev.inventoryservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(value = "inventory")
public class Inventory {

    @Id
    private String id;

    @Indexed(unique = true)
    private String productCode;
    
    @Min(value = 0, message = "Quantity cannot be less than zero")
    private Integer quantity;
}
