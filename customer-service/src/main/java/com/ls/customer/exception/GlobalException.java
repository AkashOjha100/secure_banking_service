package com.ls.customer.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(ResourceNotFoundException.class)
    public Map<String , String> handleNotFound(ResourceNotFoundException e) {

        return Map.of("error",e.getMessage());
    }
}
