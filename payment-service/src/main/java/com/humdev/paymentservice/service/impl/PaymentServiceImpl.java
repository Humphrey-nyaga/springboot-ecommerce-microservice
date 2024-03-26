package com.humdev.paymentservice.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.humdev.paymentservice.entity.Payment;
import com.humdev.paymentservice.exception.MissingDateRangeException;
import com.humdev.paymentservice.exception.PaymentNotFoundException;
import com.humdev.paymentservice.model.PaymentRequestDto;
import com.humdev.paymentservice.model.PaymentResponseDto;
import com.humdev.paymentservice.repository.PaymentRepository;
import com.humdev.paymentservice.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {

    public final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
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
    public PaymentResponseDto savePayment(PaymentRequestDto PaymentRequestDto) {

        // TODO logic to validate order, amounts etc
        Payment newPayment = paymentRepository.save(this.mapToPaymentEntity(PaymentRequestDto));
        return this.mapToPaymentResponseDto(newPayment);
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
