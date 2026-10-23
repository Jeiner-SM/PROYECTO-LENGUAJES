package com.example.sistemadereclutamiento.oferta.repository;

import com.example.sistemadereclutamiento.oferta.dto.response.EmpresaOfertaStatsDTO;
import com.example.sistemadereclutamiento.oferta.dto.response.OfertaResponseDTO;
import com.example.sistemadereclutamiento.oferta.entity.Oferta;
import com.example.sistemadereclutamiento.oferta.entity.OfertaEstado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OfertaRepository extends JpaRepository<Oferta, Long> {

    @Query("""
            SELECT new com.example.sistemadereclutamiento.oferta.dto.response.OfertaResponseDTO(
                          o.id,
                          o.titulo,
                          o.descripcion,
                          o.ubicacion,
                          o.salario,
                        CAST(o.modalidad AS string),
                        CAST(o.estado AS string),
                          e.nombreEmpresa
                        ) FROM Oferta o
                                  LEFT  join o.empresa e
            """)
    Page<OfertaResponseDTO> listarOfertas(Pageable pageable);

    @Query("""
            SELECT new com.example.sistemadereclutamiento.oferta.dto.response.OfertaResponseDTO(
                          o.id,
                          o.titulo,
                          o.descripcion,
                          o.ubicacion,
                          o.salario,
                        CAST(o.modalidad AS string),
                        CAST(o.estado AS string),
                          e.nombreEmpresa
                        ) FROM Oferta o
                                   join o.empresa e
                                              where e.usuario.id= :usuarioId
            """)
    Page<OfertaResponseDTO> listarOfertasEmpresa(Pageable pageable, @Param("usuarioId") Long usuarioId);

    @Query("""
            SELECT new com.example.sistemadereclutamiento.oferta.dto.response.OfertaResponseDTO(
                          o.id,
                          o.titulo,
                          o.descripcion,
                          o.ubicacion,
                          o.salario,
                        CAST(o.modalidad AS string),
                        CAST(o.estado AS string),
                          e.nombreEmpresa
                        ) FROM Oferta o
                                  LEFT JOIN o.empresa e
                        WHERE o.estado = com.example.sistemadereclutamiento.oferta.entity.OfertaEstado.ACTIVA
            """)
    Page<OfertaResponseDTO> listarOfertasActivas(Pageable pageable);

    @Query("""
            SELECT new com.example.sistemadereclutamiento.oferta.dto.response.OfertaResponseDTO(
                          o.id,
                          o.titulo,
                          o.descripcion,
                          o.ubicacion,
                          o.salario,
                        CAST(o.modalidad AS string),
                        CAST(o.estado AS string),
                          e.nombreEmpresa
                        ) FROM Oferta o
                                  LEFT JOIN o.empresa e
                        WHERE o.id = :id
                          AND o.estado = com.example.sistemadereclutamiento.oferta.entity.OfertaEstado.ACTIVA
            """)
    Optional<OfertaResponseDTO> findActivaById(@Param("id") Long id);

    long countByEmpresa_Usuario_Id(Long usuarioId);

    long countByEmpresa_Usuario_IdAndEstado(Long usuarioId, OfertaEstado estado);

    // Antes obtenerEstadisticasEmpresa() hacía 3 COUNT(*) separados (total, activas,
    // cerradas). Aquí se calculan los 3 en UNA sola pasada sobre la tabla con
    // COUNT(CASE WHEN ...), reduciendo 3 round-trips a la BD a 1 para el panel de empresa.
    @Query("""
            SELECT new com.example.sistemadereclutamiento.oferta.dto.response.EmpresaOfertaStatsDTO(
                        count(o),
                        count(case when o.estado = com.example.sistemadereclutamiento.oferta.entity.OfertaEstado.ACTIVA then 1 end),
                        count(case when o.estado = com.example.sistemadereclutamiento.oferta.entity.OfertaEstado.CERRADA then 1 end)
                    )
            FROM Oferta o
            WHERE o.empresa.usuario.id = :usuarioId
            """)
    EmpresaOfertaStatsDTO obtenerEstadisticasPorUsuario(@Param("usuarioId") Long usuarioId);
}