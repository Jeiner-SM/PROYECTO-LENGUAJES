package com.example.sistemadereclutamiento.curriculum.entity;

import com.example.sistemadereclutamiento.usuario.entity.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "curriculum_vitae")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurriculumVitae {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tituloProfesional;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(columnDefinition = "TEXT")
    private String experiencia;

    @Column(columnDefinition = "TEXT")
    private String habilidades;

    @Column(columnDefinition = "TEXT")
    private String educacion;

    private String telefono;

    private String linkedin;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}