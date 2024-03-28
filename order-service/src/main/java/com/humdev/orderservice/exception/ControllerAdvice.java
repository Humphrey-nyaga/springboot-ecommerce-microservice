package com.humdev.orderservice.exception;

import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.humdev.orderservice.model.ApiResponse;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(InventoryServiceException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleInventoryServiceException(InventoryServiceException ex) {

        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build();

        return response;
    }

    @ExceptionHandler(ProductServiceException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ApiResponse<?> handleProductServiceException(ProductServiceException ex) {

        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build();

        return response;
    }

    @ExceptionHandler(InvalidStartAndEndDatesException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleInvalidStartAndEndDatesException(InvalidStartAndEndDatesException ex) {

        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build();

        return response;
    }

    @ExceptionHandler(MissingDateRangeException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleInvalidStartAndEndDatesException(MissingDateRangeException ex) {

        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build();

        return response;
    }

    @ExceptionHandler(NotEnoughQuantityException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiResponse<Map<String, Integer>> notEnoughQuantityException(NotEnoughQuantityException ex) {

        ApiResponse<Map<String, Integer>> response = ApiResponse.<Map<String, Integer>>builder()
                .success(false)
                .message(ex.getMessage())
                .data(ex.getUnavailableItems())
                .build();

        return response;
    }

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiResponse<Set<String>> handleProductNotFoundException(ProductNotFoundException ex) {

        ApiResponse<Set<String>> response = ApiResponse.<Set<String>>builder()
                .success(false)
                .message(ex.getMessage())
                .data(ex.getUnavailableProducts())
                .build();

        return response;
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(code = HttpStatus.OK)
    public ApiResponse<String> handleOrderNotFoundException(OrderNotFoundException ex) {

        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(false)
                .message(ex.getMessage())
                .build();
        return response;
    }

    @ExceptionHandler(OrderAmountDoesNotMatchException.class)
    @ResponseStatus(code = HttpStatus.OK)
    public ApiResponse<String> handleOrderAmountDoesNotMatchException(OrderAmountDoesNotMatchException ex) {

        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(false)
                .message(ex.getMessage())
                .build();
        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleAnyOtherExceptionGracefully(Exception ex) {

        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message("An Error Occurred")
                .build();

        return response;
    }

    @ExceptionHandler(OrderServiceException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleOrderServiceException(OrderServiceException ex) {

        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message("An Error Occurred")
                .build();

        return response;
    }
}
