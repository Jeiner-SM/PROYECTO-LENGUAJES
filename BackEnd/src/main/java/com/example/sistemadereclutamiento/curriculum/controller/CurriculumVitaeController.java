package com.example.sistemadereclutamiento.curriculum.controller;

import com.example.sistemadereclutamiento.curriculum.dto.CurriculumVitaeRequestDTO;
import com.example.sistemadereclutamiento.curriculum.dto.CurriculumVitaeResponseDTO;
import com.example.sistemadereclutamiento.curriculum.service.CurriculumVitaeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cv")
public class CurriculumVitaeController {

    @Autowired
    private CurriculumVitaeService curriculumVitaeService;

    @GetMapping
    public ResponseEntity<List<CurriculumVitaeResponseDTO>> listarCVs() {

        return ResponseEntity.ok(
                curriculumVitaeService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurriculumVitaeResponseDTO> obtenerCV(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                curriculumVitaeService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<CurriculumVitaeResponseDTO> crearCV(
            @RequestBody CurriculumVitaeRequestDTO dto) {

        CurriculumVitaeResponseDTO nuevoCV =
                curriculumVitaeService.guardarCV(dto);

        return new ResponseEntity<>(
                nuevoCV,
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CurriculumVitaeResponseDTO> actualizarCV(
            @PathVariable Long id,
            @RequestBody CurriculumVitaeRequestDTO dto) {

        return ResponseEntity.ok(
                curriculumVitaeService.actualizarCV(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCV(
            @PathVariable Long id) {

        curriculumVitaeService.eliminarCV(id);

        return ResponseEntity.noContent().build();
    }
}