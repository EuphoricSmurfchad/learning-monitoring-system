package com.smurfchad.learning_monitoring_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.smurfchad.learning_monitoring_system.dto.error.ExceptionDtoResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDtoResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionDtoResponse(ex.getMessage()));

    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ExceptionDtoResponse> handleBusinessConflict(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionDtoResponse(ex.getMessage()));
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ExceptionDtoResponse> handleInvalidRequest(InvalidRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDtoResponse(ex.getMessage()));
    }

    @ExceptionHandler (ValidationException.class)
    public ResponseEntity<ExceptionDtoResponse> handleInvalidValidation(ValidationException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionDtoResponse(ex.getMessage()));
    }

}
