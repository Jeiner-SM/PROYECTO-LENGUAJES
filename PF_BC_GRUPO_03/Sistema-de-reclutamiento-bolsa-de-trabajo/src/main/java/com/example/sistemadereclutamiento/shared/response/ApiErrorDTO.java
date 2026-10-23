package com.example.sistemadereclutamiento.shared.response;

public record ApiErrorDTO(
        String code,
        String message,
        String timespan
)  {}
