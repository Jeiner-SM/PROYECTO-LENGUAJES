package com.example.sistemadereclutamiento.entrevista.repository;

import com.example.sistemadereclutamiento.entrevista.entity.Entrevista;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntrevistaRepository extends JpaRepository<Entrevista, Long> {

    List<Entrevista> findByPostulacionId(Long postulacionId);
}