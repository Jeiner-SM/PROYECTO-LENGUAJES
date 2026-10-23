package com.example.sistemadereclutamiento.postulante.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostulantePerfilResponseDTO {

    private Long postulanteId;
    private String nombre;
    private String apellido;
    private String email;

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
