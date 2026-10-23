package com.example.sistemadereclutamiento.oferta.controller;

import com.example.sistemadereclutamiento.oferta.dto.response.OfertaResponseDTO;
import com.example.sistemadereclutamiento.oferta.service.OfertaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ofertas/public")
@RequiredArgsConstructor
public class OfertaPostulanteController {

    private final OfertaService ofertaService;

    @GetMapping
    public ResponseEntity<Page<OfertaResponseDTO>> listarOfertasActivas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(ofertaService.obtenerOfertasPublicas(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfertaResponseDTO> obtenerOfertaActiva(@PathVariable Long id) {
        return ResponseEntity.ok(ofertaService.obtenerOfertaPublicaPorId(id));
    }
}