package com.example.sistemadereclutamiento.shared.exeption;

// Se lanza durante el login cuando un usuario con rol EMPRESA todavía tiene
// su cuenta en estado PENDIENTE (no ha sido verificada por un administrador).
public class CompanyNotVerifiedException extends ApiError {
    public CompanyNotVerifiedException(String message) {
        super(message);
    }
}
