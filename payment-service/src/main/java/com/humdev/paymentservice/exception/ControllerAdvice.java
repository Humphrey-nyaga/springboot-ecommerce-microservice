package com.humdev.paymentservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.humdev.paymentservice.model.ApiResponse;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(PaymentNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ApiResponse<?> handlePaymentNotFoundException(PaymentNotFoundException ex) {

        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build();

        return response;
    }

    @ExceptionHandler(MissingDateRangeException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleMissingDateRangeException(MissingDateRangeException ex) {

        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message(ex.getMessage())
                .build();

        return response;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<?> handleAllOtherExceptions(Exception ex) {

        ApiResponse<?> response = ApiResponse.builder()
                .success(false)
                .message("Internal Server Error")
                .build();

        return response;
    }

}
