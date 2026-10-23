package com.example.sistemadereclutamiento.oferta.entity;

import com.example.sistemadereclutamiento.empresa.entity.Empresa;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="ofertas")
@Data
@NoArgsConstructor

public class Oferta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;


    private String descripcion;


    private String ubicacion;

    @Column(nullable = false)
    private Double salario;

    @Column(length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private Modalidad modalidad = Modalidad.PRESENCIAL;

    @Column(length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private OfertaEstado estado = OfertaEstado.ACTIVA;

    @ManyToOne
    @JoinColumn(name="empresa_id", nullable = false)
    private Empresa empresa;
}