package com.humdev.paymentservice.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.humdev.paymentservice.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByPaymentCode(String paymentCode);

    Optional<Payment> findByOrderId(String orderId);

    public List<Payment> findByDateRange(LocalDateTime startDate, LocalDateTime endDate);
}
