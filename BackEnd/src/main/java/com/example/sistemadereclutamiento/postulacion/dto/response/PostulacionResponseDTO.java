package com.example.sistemadereclutamiento.postulacion.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PostulacionResponseDTO {
    private Long id;
    private Long postulanteId;
    private String nombrePostulante;
    private String emailPostulante;
    private Long ofertaId;
    private String ofertaTitulo;
    private String nombreEmpresa;
    private String cvUrl;
    // Id del currículum estructurado (Mi Perfil / CV) del postulante, si lo completó.
    // Permite a la empresa consultarlo vía GET /api/cv/{id} cuando no hay cvUrl.
    private Long curriculumId;
    private String estado;
    private LocalDateTime fechaPostulacion;
}