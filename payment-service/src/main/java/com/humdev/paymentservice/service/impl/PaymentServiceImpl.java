package com.humdev.paymentservice.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.humdev.paymentservice.model.PaymentRequestDto;
import com.humdev.paymentservice.model.PaymentResponseDto;
import com.humdev.paymentservice.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public PaymentResponseDto findPaymentById(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PaymentResponseDto findPaymentByOrderId(String orderId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PaymentResponseDto findPaymentByPaymentCode(String paymentCode) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<PaymentResponseDto> findPaymentsByDateRange(Long id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PaymentResponseDto savePayment(PaymentRequestDto PaymentRequestDto) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
