package com.example.sistemadereclutamiento.contacto.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MensajeContactoResponseDTO {
    private Long id;
    private String nombre;
    private String email;
    private String asunto;
    private String mensaje;
    private LocalDateTime fechaEnvio;
}
