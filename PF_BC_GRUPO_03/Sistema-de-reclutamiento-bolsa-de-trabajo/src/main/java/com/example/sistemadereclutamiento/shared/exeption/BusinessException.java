package com.example.sistemadereclutamiento.shared.exeption;

public class BusinessException extends ApiError {
    public BusinessException(String message) {
        super(message);
    }
}
