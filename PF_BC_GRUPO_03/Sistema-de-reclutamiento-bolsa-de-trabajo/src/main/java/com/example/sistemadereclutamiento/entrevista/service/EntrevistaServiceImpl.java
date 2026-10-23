package com.example.sistemadereclutamiento.entrevista.service;

import com.example.sistemadereclutamiento.entrevista.dto.request.EntrevistaRequestDTO;
import com.example.sistemadereclutamiento.entrevista.dto.response.EntrevistaResponseDTO;
import com.example.sistemadereclutamiento.entrevista.entity.Entrevista;

import org.springframework.transaction.annotation.Transactional;

import com.example.sistemadereclutamiento.entrevista.repository.EntrevistaRepository;
import com.example.sistemadereclutamiento.postulacion.entity.EstadoPostulacion;
import com.example.sistemadereclutamiento.postulacion.entity.Postulacion;
import com.example.sistemadereclutamiento.postulacion.repository.PostulacionRepository;
import com.example.sistemadereclutamiento.shared.exeption.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntrevistaServiceImpl implements EntrevistaService {

    @Autowired
    private EntrevistaRepository entrevistaRepository;

    @Autowired
    private PostulacionRepository postulacionRepository;

    @Override
    @Transactional
    public EntrevistaResponseDTO crear(EntrevistaRequestDTO dto) {

        Postulacion postulacion = postulacionRepository.findById(dto.getPostulacionId())
                .orElseThrow(() -> new ResourceNotFoundException("Postulación no encontrada"));

        Entrevista entrevista = new Entrevista();

        entrevista.setFecha(dto.getFecha());
        entrevista.setModalidad(dto.getModalidad());
        entrevista.setEnlace(dto.getEnlace());
        entrevista.setObservacion(dto.getObservacion());
        entrevista.setPostulacion(postulacion);

        // actualizar estado
        postulacion.setEstado(EstadoPostulacion.ENTREVISTA);

        Entrevista guardada = entrevistaRepository.save(entrevista);

        return mapear(guardada);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntrevistaResponseDTO> listar() {

        return entrevistaRepository.findAll()
                .stream()
                .map(this::mapear)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EntrevistaResponseDTO obtener(Long id) {

        Entrevista entrevista = entrevistaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrevista no encontrada"));

        return mapear(entrevista);
    }

    private EntrevistaResponseDTO mapear(Entrevista e) {

        EntrevistaResponseDTO dto = new EntrevistaResponseDTO();

        dto.setId(e.getId());
        dto.setPostulacionId(e.getPostulacion().getId());

        dto.setPostulanteNombre(
                e.getPostulacion()
                 .getPostulante()
                 .getUsuario()
                 .getNombre()
        );

        dto.setOfertaTitulo(
                e.getPostulacion()
                 .getOferta()
                 .getTitulo()
        );

        dto.setFecha(e.getFecha().toString());
        dto.setModalidad(e.getModalidad());
        dto.setEnlace(e.getEnlace());
        dto.setObservacion(e.getObservacion());

        return dto;
    }
}