package com.humdev.paymentservice.model;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

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