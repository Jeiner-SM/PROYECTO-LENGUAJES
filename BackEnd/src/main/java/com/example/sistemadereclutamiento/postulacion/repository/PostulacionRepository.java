package com.example.sistemadereclutamiento.postulacion.repository;

import com.example.sistemadereclutamiento.postulacion.entity.EstadoPostulacion;
import com.example.sistemadereclutamiento.postulacion.entity.Postulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostulacionRepository extends JpaRepository<Postulacion, Long> {

    // Las 3 queries de listado usan JOIN FETCH para traer postulante -> usuario y
    // oferta -> empresa en UNA sola consulta SQL. Sin esto, al mapear cada fila a DTO
    // (mapearADto accede a p.getPostulante().getUsuario() y p.getOferta().getEmpresa())
    // Hibernate dispara hasta 3 queries extra POR CADA postulación (N+1).
    @Query("""
        SELECT p FROM Postulacion p
            JOIN FETCH p.postulante pos
            JOIN FETCH pos.usuario
            JOIN FETCH p.oferta o
            JOIN FETCH o.empresa
        WHERE p.postulante.id = :postulanteId
        ORDER BY p.fechaPostulacion DESC
    """)
    List<Postulacion> findByPostulanteId(@Param("postulanteId") Long postulanteId);

    @Query("""
        SELECT p FROM Postulacion p
            JOIN FETCH p.postulante pos
            JOIN FETCH pos.usuario
            JOIN FETCH p.oferta o
            JOIN FETCH o.empresa
        WHERE p.oferta.id = :ofertaId
        ORDER BY p.fechaPostulacion DESC
    """)
    List<Postulacion> findByOfertaId(@Param("ofertaId") Long ofertaId);

    @Query("SELECT p FROM Postulacion p JOIN FETCH p.postulante pos JOIN FETCH pos.usuario JOIN FETCH p.oferta o JOIN FETCH o.empresa WHERE p.id = :id")
    Optional<Postulacion> findByIdDetailed(@Param("id") Long id);

    @Query("""
        SELECT p FROM Postulacion p
            JOIN FETCH p.postulante pos
            JOIN FETCH pos.usuario
            JOIN FETCH p.oferta o
            JOIN FETCH o.empresa emp
        WHERE p.estado = :estado
          AND emp.id = :empresaId
        ORDER BY p.fechaPostulacion DESC
    """)
    List<Postulacion> findByEstadoAndOfertaEmpresaId(@Param("estado") EstadoPostulacion estado, @Param("empresaId") Long empresaId);

    boolean existsByPostulanteIdAndOfertaId(Long postulanteId, Long ofertaId);

    Optional<Postulacion> findByPostulanteIdAndOfertaId(Long postulanteId, Long ofertaId);
}