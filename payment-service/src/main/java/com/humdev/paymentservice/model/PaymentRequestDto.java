package com.humdev.paymentservice.model;

import java.math.BigDecimal;

import com.humdev.paymentservice.entity.PaymentMethod;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PaymentRequestDto {
    
    @NotNull @NotBlank
    private String orderId;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String paymentProvider;

    // @Min(value = 1, message = "Payment amount cannot be less than 1")
    // private BigDecimal amount;
}
