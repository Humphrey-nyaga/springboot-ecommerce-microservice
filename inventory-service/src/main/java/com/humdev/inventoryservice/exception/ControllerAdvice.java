package com.humdev.inventoryservice.exception;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.humdev.inventoryservice.model.ApiResponse;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(NotEnoughQuantityException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ApiResponse<?> notEnoughQuantityException(NotEnoughQuantityException ex) {

        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .data(ex.getUnavailableItems())
                .build();

        return response;
    } 

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleDuplicateKeyException(DuplicateKeyException ex) {

        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(false)
                .message("Product with given ProductCode Already Exists!!")
                .build();

        return response;
    }
}