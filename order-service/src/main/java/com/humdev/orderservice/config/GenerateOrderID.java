package com.humdev.orderservice.config;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenerateOrderID {

    private static final AtomicLong counter = new AtomicLong(0);

    @Bean
    public String generateOrderUUIDString() {
        UUID uuid = UUID.randomUUID();
        long uniqueId = counter.getAndIncrement();
        return "ORD" + "-" + uuid.toString() + "-" + uniqueId;
    }

}
