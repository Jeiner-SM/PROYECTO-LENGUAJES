package com.example.sistemadereclutamiento.postulante.controller;

import com.example.sistemadereclutamiento.postulante.dto.request.PostulantePerfilRequestDTO;
import com.example.sistemadereclutamiento.postulante.dto.response.PostulanteMeResponseDTO;
import com.example.sistemadereclutamiento.postulante.dto.response.PostulantePerfilResponseDTO;
import com.example.sistemadereclutamiento.postulante.service.PostulanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/postulantes")
@RequiredArgsConstructor
public class PostulanteController {

    private final PostulanteService postulanteService;

    @GetMapping("/me")
    public ResponseEntity<PostulanteMeResponseDTO> obtenerPostulanteActual() {
        return ResponseEntity.ok(postulanteService.obtenerPostulanteActual());
    }

    // Fase 4: "Mi Perfil / CV" del postulante autenticado
    @GetMapping("/mi-perfil")
    public ResponseEntity<PostulantePerfilResponseDTO> obtenerMiPerfil() {
        return ResponseEntity.ok(postulanteService.obtenerMiPerfil());
    }

    @PutMapping("/mi-perfil")
    public ResponseEntity<PostulantePerfilResponseDTO> actualizarMiPerfil(
            @RequestBody PostulantePerfilRequestDTO dto) {
        return ResponseEntity.ok(postulanteService.actualizarMiPerfil(dto));
    }
}