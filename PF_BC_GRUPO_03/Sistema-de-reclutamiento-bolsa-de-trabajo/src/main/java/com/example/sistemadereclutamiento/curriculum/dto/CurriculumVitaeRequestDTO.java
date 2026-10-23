package com.example.sistemadereclutamiento.curriculum.dto;

import lombok.Data;

@Data
public class CurriculumVitaeRequestDTO {

    private String tituloProfesional;
    private String descripcion;
    private String experiencia;
    private String habilidades;
    private String educacion;
    private String telefono;
    private String linkedin;
    private Long usuarioId;
}