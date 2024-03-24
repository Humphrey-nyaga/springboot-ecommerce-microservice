package com.humdev.inventoryservice.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.humdev.inventoryservice.model.ApiResponse;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(NotEnoughQuantityException.class)
    @ResponseStatus(code = HttpStatus.OK)
    public ApiResponse<Map<String, Integer>> notEnoughQuantityException(NotEnoughQuantityException ex) {

        ApiResponse<Map<String, Integer>> response = ApiResponse.<Map<String, Integer>>builder()
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

    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleProductNotFoundException(ProductNotFoundException ex) {

        ApiResponse<String> response = ApiResponse.<String>builder()
                .success(false)
                .message(ex.getMessage())
                .build();

        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
