package com.example.sistemadereclutamiento.entrevista.service;

import com.example.sistemadereclutamiento.entrevista.dto.request.EntrevistaRequestDTO;
import com.example.sistemadereclutamiento.entrevista.dto.response.EntrevistaResponseDTO;

import java.util.List;

public interface EntrevistaService {

    EntrevistaResponseDTO crear(EntrevistaRequestDTO dto);

    List<EntrevistaResponseDTO> listar();

    EntrevistaResponseDTO obtener(Long id);
}