package com.example.sistemadereclutamiento.curriculum.repository;

import com.example.sistemadereclutamiento.curriculum.entity.CurriculumVitae;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CurriculumVitaeRepository extends JpaRepository<CurriculumVitae, Long> {

    Optional<CurriculumVitae> findByUsuario_Id(Long usuarioId);

    // Carga en lote (1 query, WHERE usuario_id IN (...)) los CV de varios postulantes
    // a la vez, para evitar 1 consulta por fila al listar postulaciones (N+1).
    List<CurriculumVitae> findByUsuario_IdIn(List<Long> usuarioIds);
}