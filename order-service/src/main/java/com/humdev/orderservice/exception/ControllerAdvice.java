package com.humdev.orderservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.humdev.orderservice.model.ApiResponse;

@RestControllerAdvice
public class ControllerAdvice {
    // @ExceptionHandler(Exception.class)
    // @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    // public ApiResponse<?> handleAnyOtherExceptionGracefully(Exception ex) {

    //     ApiResponse<?> response = ApiResponse.builder()
    //             .success(false)
    //             .message(ex.getMessage())
    //             .build();

    //     return response;
    // } 
}
