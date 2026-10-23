package com.example.sistemadereclutamiento.shared.handler;

import com.example.sistemadereclutamiento.shared.exeption.BusinessException;
import com.example.sistemadereclutamiento.shared.exeption.CompanyNotVerifiedException;
import com.example.sistemadereclutamiento.shared.exeption.ResourceNotFoundException;
import com.example.sistemadereclutamiento.shared.response.ApiErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiErrorDTO("RESOURCE_NOT_FOUND",
                        ex.getMessage(),
                        LocalDateTime.now().toString())
        );
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiErrorDTO> handleBusinessException(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorDTO("BUSINESS_EXCEPTION",
                        ex.getMessage(),
                        LocalDateTime.now().toString())
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream().map(
                e->e.getField() + ": " + e.getDefaultMessage()
        ).collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorDTO("VALIDATION_EXCEPTION",
                        message,
                        LocalDateTime.now().toString())
        );

    }

    // Evita 500 cuando el body enviado tiene un formato inválido
    // (ej. fechas vacías/mal formadas), devolviendo un 400 claro en su lugar.
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorDTO> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorDTO("INVALID_REQUEST_BODY",
                        "El formato de los datos enviados no es válido. Verifica los campos e intenta nuevamente.",
                        LocalDateTime.now().toString())
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorDTO> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ApiErrorDTO("DATA_INTEGRITY_VIOLATION",
                        "La operación no pudo completarse por una restricción de datos.",
                        LocalDateTime.now().toString())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorDTO> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiErrorDTO(
                        "INTERNAL_SERVER_ERROR",
                        "Internal Server Error",
                        LocalDateTime.now().toString()
                )
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorDTO> handleBadCredentials() {

        ApiErrorDTO error = new ApiErrorDTO(
                "AUTH_INVALID_CREDENTIALS",
                "Usuario o contraseña incorrectos",
                LocalDateTime.now().toString()
        );

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(error);
    }

    // Cuenta deshabilitada o bloqueada (Usuario.activo = false).
    // Se devuelve un código específico "ACCOUNT_DISABLED" para que el frontend
    // pueda mostrar el mensaje exacto y redirigir a /contacto.
    // Cubre tanto DisabledException (isEnabled() = false) como LockedException
    // (isAccountNonLocked() = false), que Spring Security lanza durante las
    // preAuthenticationChecks del AuthenticationManager.
    @ExceptionHandler({DisabledException.class, LockedException.class})
    public ResponseEntity<ApiErrorDTO> handleDisabledException(AccountStatusException ex) {
        return accountDisabledResponse();
    }

    // Red de seguridad: si el estado deshabilitado/bloqueado del usuario llega envuelto
    // en un InternalAuthenticationServiceException (por ejemplo, porque se lanzó una
    // excepción de cuenta dentro de un UserDetailsService.loadUserByUsername, donde
    // DaoAuthenticationProvider.retrieveUser() envuelve cualquier excepción que no sea
    // UsernameNotFoundException), desenvolvemos la causa real y respondemos igual con
    // 403 ACCOUNT_DISABLED en vez de dejar que caiga en el 500 genérico.
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ApiErrorDTO> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException ex) {
        Throwable cause = ex.getCause();

        if (cause instanceof AccountStatusException) {
            return accountDisabledResponse();
        }

        ApiErrorDTO error = new ApiErrorDTO(
                "AUTH_SERVICE_ERROR",
                "Ocurrió un error al procesar la autenticación. Intenta nuevamente.",
                LocalDateTime.now().toString()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    // Empresa aún no verificada por un administrador (Empresa.estadoValidacion = PENDIENTE).
    // Código específico "COMPANY_NOT_VERIFIED" para que el frontend distinga este caso
    // del de cuenta deshabilitada y muestre un mensaje acorde.
    @ExceptionHandler(CompanyNotVerifiedException.class)
    public ResponseEntity<ApiErrorDTO> handleCompanyNotVerifiedException(CompanyNotVerifiedException ex) {

        ApiErrorDTO error = new ApiErrorDTO(
                "COMPANY_NOT_VERIFIED",
                ex.getMessage(),
                LocalDateTime.now().toString()
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(error);
    }

    private ResponseEntity<ApiErrorDTO> accountDisabledResponse() {
        ApiErrorDTO error = new ApiErrorDTO(
                "ACCOUNT_DISABLED",
                "Tu cuenta ha sido deshabilitada por un administrador.",
                LocalDateTime.now().toString()
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(error);
    }

    @ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
    public ResponseEntity<ApiErrorDTO> handleAuthorizationDenied() {

        ApiErrorDTO error = new ApiErrorDTO(
                "AUTH_FORBIDDEN",
                "No tienes permisos para esta acción",
                LocalDateTime.now().toString()
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(error);
    }
}
