package com.example.sistemadereclutamiento.oferta.controller;

import com.example.sistemadereclutamiento.oferta.dto.request.OfertaEstadoUpdateDTO;
import com.example.sistemadereclutamiento.oferta.dto.request.OfertaRequestDTO;
import com.example.sistemadereclutamiento.oferta.dto.request.OfertaUpdateDTO;
import com.example.sistemadereclutamiento.oferta.dto.response.EmpresaOfertaStatsDTO;
import com.example.sistemadereclutamiento.oferta.dto.response.OfertaResponseDTO;
import com.example.sistemadereclutamiento.oferta.service.OfertaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

// Todos los endpoints de este controlador son exclusivos de ROLE_EMPRESA.
@RestController
@RequestMapping("/api/empresa/ofertas")
@RequiredArgsConstructor
@PreAuthorize("hasRole('EMPRESA')")
public class OfertaEmpresaController {

    private final OfertaService ofertaService;

    @PreAuthorize("hasRole('EMPRESA') and hasAuthority('OFERTA_VIEW')")
    @GetMapping
    public ResponseEntity<Page<OfertaResponseDTO>> listarOfertasEmpresa(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                ofertaService.obtenerOfertasEmpresa(pageable)
        );
    }

    // Estadísticas para las tarjetas del Panel de Control (total / activas / cerradas).
    @GetMapping("/stats")
    public ResponseEntity<EmpresaOfertaStatsDTO> obtenerEstadisticas() {
        return ResponseEntity.ok(ofertaService.obtenerEstadisticasEmpresa());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfertaResponseDTO> obtenerOferta(@PathVariable Long id) {
        return ResponseEntity.ok(ofertaService.obtenerOfertaEmpresaPorId(id));
    }

    @PreAuthorize("hasRole('EMPRESA') and hasAuthority('OFERTA_CREATE')")
    @PostMapping
    public ResponseEntity<OfertaResponseDTO> crearOferta(
            @RequestBody OfertaRequestDTO requestDTO) {

        OfertaResponseDTO nuevaOferta =
                ofertaService.guardarOferta(requestDTO);

        return new ResponseEntity<>(nuevaOferta, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('EMPRESA') and hasAuthority('OFERTA_UPDATE')")
    @PatchMapping("/{id}")
    public ResponseEntity<OfertaResponseDTO> actualizarOferta(
            @PathVariable Long id,
            @RequestBody OfertaUpdateDTO requestDTO) {

        return ResponseEntity.ok(
                ofertaService.actualizarOferta(id, requestDTO));
    }

    // Cambio rápido de estado (ej. ACTIVA -> CERRADA) desde la tabla de "Gestionar Ofertas".
    // Solo el dueño de la oferta puede cambiarla (validado en OfertaService.validarPropietario).
    @PreAuthorize("hasRole('EMPRESA') and hasAuthority('OFERTA_UPDATE')")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<OfertaResponseDTO> cambiarEstadoOferta(
            @PathVariable Long id,
            @RequestBody OfertaEstadoUpdateDTO requestDTO) {

        return ResponseEntity.ok(
                ofertaService.cambiarEstadoOferta(id, requestDTO));
    }

    @PreAuthorize("hasRole('EMPRESA') and hasAuthority('OFERTA_DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarOferta(@PathVariable Long id) {

        ofertaService.eliminarOferta(id);

        return ResponseEntity.noContent().build();
    }
}
