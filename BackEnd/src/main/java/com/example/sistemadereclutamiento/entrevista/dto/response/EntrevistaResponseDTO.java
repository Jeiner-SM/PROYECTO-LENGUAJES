package com.example.sistemadereclutamiento.entrevista.dto.response;

import lombok.Data;

@Data
public class EntrevistaResponseDTO {

    private Long id;

    private Long postulacionId;

    private String postulanteNombre;

    private String ofertaTitulo;

    private String fecha;

    private String modalidad;

    private String enlace;

    private String observacion;
}