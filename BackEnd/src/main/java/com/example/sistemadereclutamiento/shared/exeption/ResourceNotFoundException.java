package com.example.sistemadereclutamiento.shared.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class ResourceNotFoundException extends ApiError {
    
    public ResourceNotFoundException(String message) {
        super(message);
    }
}