package com.humdev.paymentservice.service;

import java.time.LocalDate;
import java.util.List;


import com.humdev.paymentservice.model.PaymentRequestDto;
import com.humdev.paymentservice.model.PaymentResponseDto;

public interface PaymentService {

    public PaymentResponseDto savePayment(PaymentRequestDto PaymentRequestDto);

    public PaymentResponseDto findPaymentByPaymentCode(String paymentCode);

    public PaymentResponseDto findPaymentByOrderId(String orderId);

    public PaymentResponseDto findPaymentById(Long id);

    public List<PaymentResponseDto> findPaymentsByDateRange(LocalDate startDate, LocalDate endDate);

    public List<PaymentResponseDto> findAllPayments();

}
