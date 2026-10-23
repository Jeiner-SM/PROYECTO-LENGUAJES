package com.example.sistemadereclutamiento.usuario.repository;

import com.example.sistemadereclutamiento.usuario.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepositorio  extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    @Query("""
     select u FROM Usuario u 
         join fetch u.roles r
             join fetch r.permisos
                 where u.email= :email
    """)
    Optional<Usuario> existsByEmailAndPermiso(String email);

    // Panel admin: lista postulantes y empresas, pero nunca otras cuentas de administrador.
    // "rol" y "activo" son opcionales (si llegan null, esa condición se ignora), lo que permite
    // reutilizar este mismo método tanto para la pestaña de Postulantes (rol='POSTULANTE')
    // como para un listado general filtrado solo por estado habilitado/deshabilitado.
    @Query("""
        select distinct u from Usuario u
            join u.roles r
                where r.nombre <> 'ADMIN'
                and (:rol is null or r.nombre = :rol)
                and (:activo is null or u.activo = :activo)
                order by u.fechaCreacion desc
    """)
    Page<Usuario> findAllExceptAdmin(
            @Param("rol") String rol,
            @Param("activo") Boolean activo,
            Pageable pageable
    );
}
