package com.example.sistemadereclutamiento.postulante.dto.request;

import com.example.sistemadereclutamiento.usuario.dto.request.UsuarioRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PostulanteRequest(
        @Valid @NotNull(message = "Los datos de usuario son obligatorios") UsuarioRequestDTO usuario,
        String direccion,
        String genero,
        LocalDate fechaNacimiento
) {}
