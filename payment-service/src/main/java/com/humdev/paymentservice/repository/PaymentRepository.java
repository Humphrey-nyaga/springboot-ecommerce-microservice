package com.humdev.paymentservice.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.humdev.paymentservice.entity.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

        @Query("SELECT p " +
                        "FROM Payment p " +
                        "WHERE p.paymentDate >= :startDate AND p.paymentDate < :endDate")
        public List<Payment> findByPaymentDateBetween(@Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        @Query("SELECT p " +
                        "FROM Payment p " +
                        "WHERE p.paymentDate >= :startDate")
        List<Payment> findByPaymentDateOnOrAfter(LocalDateTime startDate);

        @Query("SELECT p " +
                        "FROM Payment p " +
                        "WHERE p.paymentDate <= :endDate")
        List<Payment> findByPaymentDateBeforeOrOn(@Param("endDate") LocalDateTime endDate);

        Optional<Payment> findByPaymentCode(String paymentCode);

        Optional<Payment> findByOrderId(String orderId);
}
