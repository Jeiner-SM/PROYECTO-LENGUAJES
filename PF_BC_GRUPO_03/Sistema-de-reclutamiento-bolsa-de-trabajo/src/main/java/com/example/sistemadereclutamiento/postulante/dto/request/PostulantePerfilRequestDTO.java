package com.example.sistemadereclutamiento.postulante.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PostulantePerfilRequestDTO {

    private String nombre;
    private String apellido;

    private LocalDate fechaNacimiento;
    private String genero;
    private String direccion;

    // Datos del CV (currículum estructurado del postulante)
    private String tituloProfesional;
    private String descripcion;
    private String experiencia;
    private String habilidades;
    private String educacion;
    private String telefono;
    private String linkedin;
}
