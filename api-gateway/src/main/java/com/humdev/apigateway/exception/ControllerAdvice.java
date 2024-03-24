package com.humdev.apigateway.exception;


import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> notNotFoundException(NotFoundException ex) {

        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message("An Error Occurred")
                .build();

        return response;
    }
    
}
// 