package com.smurfchad.learning_monitoring_system.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor 
@Getter
public class ErrorResponse {
    private final String message;
}
