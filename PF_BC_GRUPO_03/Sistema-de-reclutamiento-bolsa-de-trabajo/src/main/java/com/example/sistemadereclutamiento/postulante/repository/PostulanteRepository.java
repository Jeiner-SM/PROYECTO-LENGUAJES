package com.example.sistemadereclutamiento.postulante.repository;

import com.example.sistemadereclutamiento.postulante.entity.Postulante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostulanteRepository extends JpaRepository<Postulante, Long> {

    Optional<Postulante> findByUsuario_Id(Long usuarioId);
}