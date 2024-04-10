package com.humdev.productservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductResponse {
    private Long id;

    private String name;

    private BigDecimal price;

    private String productCode;

    @JsonProperty("image")
    private String imageUrl;
    
    private Long categoryId;


}
