package com.humdev.productservice.model;

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

    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private int itemCount;

    private boolean success;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private T data;

}