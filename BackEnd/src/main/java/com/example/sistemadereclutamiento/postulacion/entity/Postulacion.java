package com.example.sistemadereclutamiento.postulacion.entity;

import com.example.sistemadereclutamiento.oferta.entity.Oferta;
import com.example.sistemadereclutamiento.postulante.entity.Postulante;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "postulaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Postulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postulante_id", nullable = false)
    private Postulante postulante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oferta_id", nullable = false)
    private Oferta oferta;

    // insertable=false porque la BD la completa con su valor DEFAULT al crear el registro;
    // se permite actualizarla para poder refrescarla en una re-postulación (ver PostulacionServiceImpl).
    @Column(name = "fecha_postulacion", insertable = false)
    private LocalDateTime fechaPostulacion;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private EstadoPostulacion estado = EstadoPostulacion.ENVIADO;

    @Column(name = "cv_url")
    private String cvUrl;
}