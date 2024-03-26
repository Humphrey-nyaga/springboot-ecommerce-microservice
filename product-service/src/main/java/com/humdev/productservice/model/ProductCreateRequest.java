package com.humdev.productservice.model;

import java.math.BigDecimal;

import org.springframework.lang.NonNull;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequest {

    @NonNull
    private String name;

    @Min(value=1, message="Product price cannot be less than 1")
    private BigDecimal price;

    @NonNull
    private String productCode;

}
