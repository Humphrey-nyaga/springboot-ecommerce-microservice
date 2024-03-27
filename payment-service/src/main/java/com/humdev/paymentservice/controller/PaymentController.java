package com.humdev.paymentservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.humdev.paymentservice.model.ApiResponse;
import com.humdev.paymentservice.model.PaymentRequestDto;
import com.humdev.paymentservice.model.PaymentResponseDto;
import com.humdev.paymentservice.service.PaymentService;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/v1/payments")
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/order/{orderId}")
    public ApiResponse<PaymentResponseDto> filterPaymentByOrderId(@PathVariable String orderId) {

        PaymentResponseDto paymentResponseDto = paymentService.findPaymentByOrderId(orderId);
        ApiResponse<PaymentResponseDto> response = ApiResponse.<PaymentResponseDto>builder()
                .message("Payment retrieved successfully")
                .success(true)
                .itemCount(1)
                .data(paymentResponseDto)
                .build();
        return response;

    }

    @PostMapping("/paymentCode/{paymentCode}")
    public ApiResponse<PaymentResponseDto> filterPaymentByCode(@PathVariable String paymentCode) {

        PaymentResponseDto paymentResponseDto = paymentService.findPaymentByPaymentCode(paymentCode);
        ApiResponse<PaymentResponseDto> response = ApiResponse.<PaymentResponseDto>builder()
                .message("Payment retrieved successfully")
                .success(true)
                .itemCount(1)
                .data(paymentResponseDto)
                .build();
        return response;

    }

    @PostMapping("/process")
    public ApiResponse<?> createPayment(
            @RequestParam("orderId") String orderId,
            @RequestBody PaymentRequestDto paymentRequestDto) {

        if (orderId.equals(paymentRequestDto.getOrderId())) {

            PaymentResponseDto paymentResponseDto = paymentService.savePayment(paymentRequestDto);
            ApiResponse<PaymentResponseDto> response = ApiResponse.<PaymentResponseDto>builder()
                    .message("Payment placed successfully")
                    .success(true)
                    .itemCount(1)
                    .data(paymentResponseDto)
                    .build();
            return response;
        } else {
            ApiResponse<?> response = ApiResponse.builder()
                    .message("Order ids do not match")
                    .success(true)
                    .build();
            return response;
        }

    }

    @GetMapping("/")
    public ApiResponse<List<PaymentResponseDto>> getAllPayments() {

        List<PaymentResponseDto> paymentResponseDtos = paymentService.findAllPayments();

        ApiResponse<List<PaymentResponseDto>> response = ApiResponse.<List<PaymentResponseDto>>builder()
                .message("Payments retrieved successfully")
                .success(true)
                .itemCount(paymentResponseDtos.size())
                .data(paymentResponseDtos)
                .build();
        return response;
    }

    @GetMapping("/filter")
    public ApiResponse<List<PaymentResponseDto>> filterOrders(
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate) {

        List<PaymentResponseDto> payments = paymentService.findPaymentsByDateRange(startDate, endDate);
        log.info(":::::::::::Start Date::::::::::: " + startDate);
        log.info(":::::::::::End Date::::::::::: " + endDate);
        ApiResponse<List<PaymentResponseDto>> response = ApiResponse.<List<PaymentResponseDto>>builder()
                .message("Payments retrieved successfully")
                .success(true)
                .itemCount(payments.size())
                .data(payments)
                .build();
        return response;
    }

}
