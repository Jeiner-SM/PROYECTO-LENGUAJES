package com.example.sistemadereclutamiento.contacto.repository;

import com.example.sistemadereclutamiento.contacto.entity.MensajeContacto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensajeContactoRepository extends JpaRepository<MensajeContacto, Long> {

    Page<MensajeContacto> findAllByOrderByFechaEnvioDesc(Pageable pageable);
}
