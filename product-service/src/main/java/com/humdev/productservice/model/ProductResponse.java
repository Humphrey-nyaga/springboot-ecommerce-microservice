package com.humdev.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductResponse {
    private Long id;

    private String name;

    private BigDecimal price;

    private String productCode;

    private String imageUrl;
    
    private Long categoryId;


}
