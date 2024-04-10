package com.humdev.paymentservice.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ApiResponse<T> {

    private String message;

    private boolean success;

    private int itemCount;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private T data;

}