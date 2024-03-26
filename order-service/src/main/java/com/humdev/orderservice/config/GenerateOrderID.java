package com.humdev.orderservice.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenerateOrderID {

  private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
    private static final AtomicLong counter = new AtomicLong(0);

    @Bean
    public String generateOrderUUIDString() {
        LocalDateTime now = LocalDateTime.now();
        String timestamp = now.format(formatter);
        UUID uuid = UUID.randomUUID();
        long uniqueId = counter.getAndIncrement();
        return timestamp + "-" + uuid.toString() + "-" + uniqueId;
    }

}
