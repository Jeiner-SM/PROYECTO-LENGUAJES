package com.example.sistemadereclutamiento.entrevista.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EntrevistaRequestDTO {

    @NotNull(message = "El id de postulación es obligatorio")
    private Long postulacionId;

    @NotNull(message = "La fecha es obligatoria")
    @Future(message = "La fecha debe ser futura")
    private LocalDateTime fecha;

    @NotBlank(message = "La modalidad es obligatoria")
    private String modalidad;

    @NotBlank(message = "El enlace es obligatorio")
    private String enlace;

    private String observacion;

    
}
