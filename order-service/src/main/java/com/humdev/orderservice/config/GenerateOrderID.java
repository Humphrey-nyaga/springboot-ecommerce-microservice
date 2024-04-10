package com.humdev.orderservice.config;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GenerateOrderID {

    @Bean
    public String generateOrderUUIDString() {
        UUID uuid = UUID.randomUUID();
        return "ORD-" + uuid.toString();
    }

}
