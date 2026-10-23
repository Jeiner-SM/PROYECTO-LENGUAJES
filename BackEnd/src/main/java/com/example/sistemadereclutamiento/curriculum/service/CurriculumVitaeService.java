package com.example.sistemadereclutamiento.curriculum.service;

import com.example.sistemadereclutamiento.curriculum.dto.CurriculumVitaeRequestDTO;
import com.example.sistemadereclutamiento.curriculum.dto.CurriculumVitaeResponseDTO;

import com.example.sistemadereclutamiento.curriculum.entity.CurriculumVitae;

import com.example.sistemadereclutamiento.curriculum.repository.CurriculumVitaeRepository;

import com.example.sistemadereclutamiento.shared.exeption.ResourceNotFoundException;
import com.example.sistemadereclutamiento.usuario.entity.Usuario;
import com.example.sistemadereclutamiento.usuario.repository.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CurriculumVitaeService {


    private final CurriculumVitaeRepository curriculumVitaeRepository;


    private final UsuarioRepositorio usuarioRepository;

    public List<CurriculumVitaeResponseDTO> obtenerTodos() {

        return curriculumVitaeRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public CurriculumVitaeResponseDTO obtenerPorId(Long id) {

        CurriculumVitae cv = curriculumVitaeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("CV no encontrado"));

        return convertirADTO(cv);
    }

    public CurriculumVitaeResponseDTO guardarCV(
            CurriculumVitaeRequestDTO dto) {

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado"));

        CurriculumVitae cv = new CurriculumVitae();

        cv.setTituloProfesional(dto.getTituloProfesional());
        cv.setDescripcion(dto.getDescripcion());
        cv.setExperiencia(dto.getExperiencia());
        cv.setHabilidades(dto.getHabilidades());
        cv.setEducacion(dto.getEducacion());
        cv.setTelefono(dto.getTelefono());
        cv.setLinkedin(dto.getLinkedin());
        cv.setUsuario(usuario);

        return convertirADTO(
                curriculumVitaeRepository.save(cv));
    }

    public CurriculumVitaeResponseDTO actualizarCV(
            Long id,
            CurriculumVitaeRequestDTO dto) {

        CurriculumVitae cv = curriculumVitaeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("CV no encontrado"));

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado"));

        cv.setTituloProfesional(dto.getTituloProfesional());
        cv.setDescripcion(dto.getDescripcion());
        cv.setExperiencia(dto.getExperiencia());
        cv.setHabilidades(dto.getHabilidades());
        cv.setEducacion(dto.getEducacion());
        cv.setTelefono(dto.getTelefono());
        cv.setLinkedin(dto.getLinkedin());
        cv.setUsuario(usuario);

        return convertirADTO(
                curriculumVitaeRepository.save(cv));
    }

    public void eliminarCV(Long id) {

        CurriculumVitae cv = curriculumVitaeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("CV no encontrado"));

        curriculumVitaeRepository.delete(cv);
    }

    private CurriculumVitaeResponseDTO convertirADTO(
            CurriculumVitae cv) {

        CurriculumVitaeResponseDTO dto =
                new CurriculumVitaeResponseDTO();

        dto.setId(cv.getId());
        dto.setTituloProfesional(cv.getTituloProfesional());
        dto.setDescripcion(cv.getDescripcion());
        dto.setExperiencia(cv.getExperiencia());
        dto.setHabilidades(cv.getHabilidades());
        dto.setEducacion(cv.getEducacion());
        dto.setTelefono(cv.getTelefono());
        dto.setLinkedin(cv.getLinkedin());

        if (cv.getUsuario() != null) {
            dto.setNombreUsuario(cv.getUsuario().getNombre());
        }

        return dto;
    }
}