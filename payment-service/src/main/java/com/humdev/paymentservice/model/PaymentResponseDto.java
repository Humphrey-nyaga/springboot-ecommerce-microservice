package com.humdev.paymentservice.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.humdev.paymentservice.entity.PaymentMethod;
import com.humdev.paymentservice.entity.PaymentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public class PaymentResponseDto {

    private String paymentCode;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDateTime;

    private String orderId;

    private PaymentMethod paymentMethod;

    private BigDecimal amount;

    private PaymentStatus paymentStatus;
}
