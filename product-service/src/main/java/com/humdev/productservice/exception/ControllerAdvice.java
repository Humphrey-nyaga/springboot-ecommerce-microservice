package com.humdev.productservice.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.humdev.productservice.model.ApiResponse;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleProductNotFound(ProductNotFoundException ex) {

        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .data(ex.getUnavailableProducts() != null ? ex.getUnavailableProducts() : "")
                .build();
        return response;
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleCategoryNotFoundException(CategoryNotFoundException ex) {

        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build();
        return response;
    }

    // Handle all exceptions that may not have been handled gracefully
    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleAnyOtherExceptionGracefully(Exception ex) {

        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message("An error occurred")
                .build();

        return response;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleDataIntegrityViolationExceptApiResponse(DataIntegrityViolationException ex) {

        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message("A product with the product code already exists!!")
                .build();

        return response;
    }
}
