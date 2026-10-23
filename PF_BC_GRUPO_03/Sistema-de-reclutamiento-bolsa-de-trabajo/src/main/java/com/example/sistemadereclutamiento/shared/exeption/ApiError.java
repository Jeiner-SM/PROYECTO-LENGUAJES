package com.example.sistemadereclutamiento.shared.exeption;

public class ApiError extends RuntimeException {
    public ApiError(String message) {
        super(message);
    }
}
