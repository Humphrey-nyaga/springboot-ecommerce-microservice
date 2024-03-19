package com.humdev.productservice.entity;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.humdev.productservice.model.ApiResponse;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleProductNotFound(ProductNotFoundException ex){

        ApiResponse<?> response = ApiResponse.builder()
        .success(false)
        .message(ex.getMessage())
        .build();

        return response;
    }
}
