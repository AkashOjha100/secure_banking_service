package com.ls.storageservice.exception;

import com.ls.storageservice.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ApiResponse> handleFileStorageException(FileStorageException ex) {

        return ResponseEntity.badRequest()
                .body(new ApiResponse(false , ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception ex) {
        return ResponseEntity.badRequest()
                .body(new ApiResponse(false , ex.getMessage()));
    }
}
