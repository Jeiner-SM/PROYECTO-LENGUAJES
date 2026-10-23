package com.example.sistemadereclutamiento.empresa.repository;

import com.example.sistemadereclutamiento.empresa.dto.response.EmpresaResponseDTO;
import com.example.sistemadereclutamiento.empresa.entity.Empresa;
import com.example.sistemadereclutamiento.empresa.entity.EstadoValidacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    @Query(
            """
        SELECT new com.example.sistemadereclutamiento.empresa.dto.response.EmpresaResponseDTO(
                e.id,
                   e.nombreEmpresa,
                   e.razonSocial,
                   e.ruc,
                   e.estadoValidacion,
                   u.email
                ) FROM Empresa e
                        JOIN e.usuario u
        """
    )
    List<EmpresaResponseDTO> listarEmpresas();

    boolean existsByIdAndUsuario_Email(Long id, String email);

    Optional<Empresa> findEmpresasByUsuario_Id(Long id);

    // Carga en lote (1 sola query, WHERE usuario_id IN (...)) el nombre de empresa
    // de varios usuarios a la vez, para evitar N+1 al mapear listados admin.
    List<Empresa> findByUsuario_IdIn(List<Long> usuarioIds);

    boolean existsByRuc(String ruc);

    long countByEstadoValidacion(EstadoValidacion estadoValidacion);

    // Panel admin (pestaña "Empresas"): permite filtrar por cuenta habilitada/deshabilitada
    // (usuario.activo) y por estado de validación (PENDIENTE/ACTIVO/RECHAZADO). Ambos filtros
    // son opcionales: si se envía null, esa condición se ignora.
    // "join fetch e.usuario" trae la Empresa y su Usuario en UNA sola consulta SQL
    // (relación *-a-uno, no dispara el warning de paginación en memoria de Hibernate),
    // evitando una query extra por fila al leer usuario.email/activo/fechaCreacion en el DTO.
    @Query("""
        select e from Empresa e
            join fetch e.usuario u
                where (:activo is null or u.activo = :activo)
                and (:estado is null or e.estadoValidacion = :estado)
                order by u.fechaCreacion desc
    """)
    Page<Empresa> findAllForAdmin(
            @Param("activo") Boolean activo,
            @Param("estado") EstadoValidacion estado,
            Pageable pageable
    );

}