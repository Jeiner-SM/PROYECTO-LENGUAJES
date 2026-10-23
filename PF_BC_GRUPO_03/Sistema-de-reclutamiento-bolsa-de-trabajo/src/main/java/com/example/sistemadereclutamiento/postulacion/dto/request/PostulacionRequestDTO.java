package com.example.sistemadereclutamiento.postulacion.dto.request;

import lombok.Data;

@Data
public class PostulacionRequestDTO {
    private Long postulanteId;
    private Long ofertaId;
    private String cvUrl;
}