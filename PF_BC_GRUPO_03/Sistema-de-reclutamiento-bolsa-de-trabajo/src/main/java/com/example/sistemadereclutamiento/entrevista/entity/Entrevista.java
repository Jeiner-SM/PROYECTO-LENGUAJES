package com.example.sistemadereclutamiento.entrevista.entity;

import com.example.sistemadereclutamiento.postulacion.entity.Postulacion;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "entrevistas")
public class Entrevista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fecha;

    private String modalidad; // Virtual o Presencial

    private String enlace;

    private String observacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postulacion_id")
    private Postulacion postulacion;

    public Entrevista() {
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getModalidad() {
        return modalidad;
    }

    public String getEnlace() {
        return enlace;
    }

    public String getObservacion() {
        return observacion;
    }

    public Postulacion getPostulacion() {
        return postulacion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public void setModalidad(String modalidad) {
        this.modalidad = modalidad;
    }

    public void setEnlace(String enlace) {
        this.enlace = enlace;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public void setPostulacion(Postulacion postulacion) {
        this.postulacion = postulacion;
    }
}