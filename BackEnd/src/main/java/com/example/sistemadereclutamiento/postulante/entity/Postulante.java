package com.example.sistemadereclutamiento.postulante.entity;

import com.example.sistemadereclutamiento.usuario.entity.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "postulantes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Postulante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", unique = true)
    private Usuario usuario;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(length = 20)
    private String genero;

    @Column(length = 200)
    private String direccion;
}