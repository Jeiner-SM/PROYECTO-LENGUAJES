package com.example.sistemadereclutamiento.oferta.controller;

import com.example.sistemadereclutamiento.oferta.dto.request.OfertaUpdateDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.sistemadereclutamiento.oferta.dto.request.OfertaRequestDTO;
import com.example.sistemadereclutamiento.oferta.dto.response.OfertaResponseDTO;
import com.example.sistemadereclutamiento.oferta.service.OfertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/ofertas")
@RequiredArgsConstructor
public class OfertaAdminController {


    private final OfertaService ofertaService;


    @PreAuthorize("hasAuthority('OFERTA_VIEW_ALL')")
    @GetMapping
    public ResponseEntity<Page<OfertaResponseDTO>> listarOfertas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(
                ofertaService.obtenerOfertasAdmin(pageable)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfertaResponseDTO> obtenerOferta(@PathVariable Long id) {
        return ResponseEntity.ok(ofertaService.obtenerPorId(id));
    }

}