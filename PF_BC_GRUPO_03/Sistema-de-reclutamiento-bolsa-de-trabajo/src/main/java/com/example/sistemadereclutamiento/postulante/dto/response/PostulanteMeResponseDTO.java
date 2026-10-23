package com.example.sistemadereclutamiento.postulante.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostulanteMeResponseDTO {
    private Long postulanteId;
    private Long usuarioId;
    private String email;
}