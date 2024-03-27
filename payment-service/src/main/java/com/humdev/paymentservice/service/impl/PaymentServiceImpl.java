package com.humdev.paymentservice.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.humdev.paymentservice.entity.Payment;
import com.humdev.paymentservice.entity.PaymentStatus;
import com.humdev.paymentservice.exception.MissingDateRangeException;
import com.humdev.paymentservice.exception.PaymentNotFoundException;
import com.humdev.paymentservice.exception.PaymentServiceException;
import com.humdev.paymentservice.model.ApiResponse;
import com.humdev.paymentservice.model.PaymentRequestDto;
import com.humdev.paymentservice.model.PaymentResponseDto;
import com.humdev.paymentservice.repository.PaymentRepository;
import com.humdev.paymentservice.service.PaymentService;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    public final PaymentRepository paymentRepository;
    private final WebClient.Builder webClient;

    public PaymentServiceImpl(PaymentRepository paymentRepository, WebClient.Builder webClient) {
        this.paymentRepository = paymentRepository;
        this.webClient = webClient;
    }

    @Override
    public PaymentResponseDto findPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(
                () -> new PaymentNotFoundException("Payment with id " + id + " does not exists."));
        return this.mapToPaymentResponseDto(payment);
    }

    @Override
    public PaymentResponseDto findPaymentByOrderId(String orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(
                () -> new PaymentNotFoundException("Payment for order id " + orderId + " does not exists."));
        return this.mapToPaymentResponseDto(payment);
    }

    @Override
    public PaymentResponseDto findPaymentByPaymentCode(String paymentCode) {
        Payment payment = paymentRepository.findByPaymentCode(paymentCode).orElseThrow(
                () -> new PaymentNotFoundException("Payment with code " + paymentCode + " does not exists."));
        return this.mapToPaymentResponseDto(payment);
    }

    @Override
    public PaymentResponseDto savePayment(PaymentRequestDto paymentRequestDto) {

        // TODO logic to validate order, amounts etc
        // TODO -> Exceptions from this operation need to be handled more elegantly to return specific message to user 
        Boolean isValidOrder = this.validateOrderAmount(paymentRequestDto.getOrderId(), paymentRequestDto.getAmount());
        
        if (isValidOrder) {

            Payment newPayment = paymentRepository.save(this.mapToPaymentEntity(paymentRequestDto));
            
            // get new payment and set as completed then save again --- 
            newPayment.setPaymentStatus(PaymentStatus.COMPLETED);
            paymentRepository.save(newPayment);
            return this.mapToPaymentResponseDto(newPayment);

        } else {

            throw new PaymentServiceException(
                    "Payment could not be completed for order " + paymentRequestDto.getOrderId());
        }
    }

    private Boolean validateOrderAmount(String orderNumber, BigDecimal orderAmount) {

        ApiResponse<?> isValidOrder = webClient.build().get()
                .uri("http://ORDER-SERVICE/api/v1/orders/validate",
                        uriBuilder -> uriBuilder
                                .queryParam("orderNumber", orderNumber)
                                .queryParam("orderAmount", orderAmount)
                                .build())

                .retrieve()
                .onStatus(HttpStatusCode::isError,
                        clientResponse -> Mono
                                .error(new PaymentServiceException("An error occurred while processing order payment")))
                .bodyToMono(new ParameterizedTypeReference<ApiResponse<?>>() {
                })
                .block();
        log.info("::::::::::::Returning order number and amount validations:::::::::::::::::::::::");

        if (isValidOrder.isSuccess()) {
            return true;

        } else {
            // todo: check if all errors from order service are handled well
            throw new PaymentServiceException(isValidOrder.getMessage());
        }

    }

    private PaymentResponseDto mapToPaymentResponseDto(Payment payment) {
        PaymentResponseDto paymentResponseDto = new PaymentResponseDto();
        BeanUtils.copyProperties(payment, paymentResponseDto);
        return paymentResponseDto;
    }

    private Payment mapToPaymentEntity(PaymentRequestDto paymentRequestDto) {
        Payment payment = new Payment();
        BeanUtils.copyProperties(paymentRequestDto, payment);
        return payment;
    }

    @Override
    public List<PaymentResponseDto> findPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {

        if (startDate == null && endDate == null) {
            throw new MissingDateRangeException(
                    "At least one of startDate or endDate must be provided to get a payment");
        }
        if (startDate != null && endDate != null) {
            return paymentRepository
                    .findByPaymentDateBetween(startDate.atStartOfDay(), endDate.plusDays(1).atStartOfDay())
                    .stream()
                    .map(this::mapToPaymentResponseDto)
                    .toList();

        } else if (startDate != null) {
            return paymentRepository.findByPaymentDateOnOrAfter(startDate.atStartOfDay())
                    .stream()
                    .map(this::mapToPaymentResponseDto)
                    .toList();

        } else if (endDate != null) {
            return paymentRepository.findByPaymentDateBeforeOrOn(endDate.plusDays(1).atStartOfDay())
                    .stream()
                    .map(this::mapToPaymentResponseDto)
                    .toList();

        } else {
            throw new MissingDateRangeException(
                    "At least one of startDate or endDate must be provided to get a payment");
        }
    }

    @Override
    public List<PaymentResponseDto> findAllPayments() {
        return paymentRepository.findAll().stream()
                .map(this::mapToPaymentResponseDto)
                .toList();
    }

}
