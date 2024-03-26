package com.humdev.paymentservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.humdev.paymentservice.model.PaymentRequestDto;
import com.humdev.paymentservice.model.PaymentResponseDto;

public interface PaymentService {

    public PaymentResponseDto savePayment(PaymentRequestDto PaymentRequestDto);

    public PaymentResponseDto findPaymentByPaymentCode(String paymentCode);

    public PaymentResponseDto findPaymentByOrderId(String orderId);

    public PaymentResponseDto findPaymentById(Long id);

    public List<PaymentResponseDto> findPaymentsByDateRange(Long id);

}
