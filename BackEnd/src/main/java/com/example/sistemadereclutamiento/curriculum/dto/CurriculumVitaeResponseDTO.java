package com.example.sistemadereclutamiento.curriculum.dto;

import lombok.Data;

@Data
public class CurriculumVitaeResponseDTO {

    private Long id;
    private String tituloProfesional;
    private String descripcion;
    private String experiencia;
    private String habilidades;
    private String educacion;
    private String telefono;
    private String linkedin;
    private String nombreUsuario;
}