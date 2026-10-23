package com.example.sistemadereclutamiento.postulante.service;

import com.example.sistemadereclutamiento.curriculum.entity.CurriculumVitae;
import com.example.sistemadereclutamiento.curriculum.repository.CurriculumVitaeRepository;
import com.example.sistemadereclutamiento.postulante.dto.request.PostulantePerfilRequestDTO;
import com.example.sistemadereclutamiento.postulante.dto.response.PostulanteMeResponseDTO;
import com.example.sistemadereclutamiento.postulante.dto.response.PostulantePerfilResponseDTO;
import com.example.sistemadereclutamiento.postulante.entity.Postulante;
import com.example.sistemadereclutamiento.postulante.repository.PostulanteRepository;
import com.example.sistemadereclutamiento.shared.exeption.BusinessException;
import com.example.sistemadereclutamiento.shared.exeption.ResourceNotFoundException;
import com.example.sistemadereclutamiento.usuario.entity.Usuario;
import com.example.sistemadereclutamiento.usuario.repository.UsuarioRepositorio;
import com.example.sistemadereclutamiento.usuario.service.UsuarioSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostulanteService {

    private final PostulanteRepository postulanteRepository;
    private final CurriculumVitaeRepository curriculumVitaeRepository;
    private final UsuarioRepositorio usuarioRepositorio;
    private final UsuarioSecurity usuarioSecurity;

    public PostulanteMeResponseDTO obtenerPostulanteActual() {
        Usuario usuario = usuarioSecurity.usuarioLogado();

        if (!usuarioSecurity.isPostulante()) {
            throw new BusinessException("Solo los postulantes pueden acceder a este recurso");
        }

        Postulante postulante = postulanteRepository.findByUsuario_Id(usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postulante no encontrado"));

        return new PostulanteMeResponseDTO(
                postulante.getId(),
                usuario.getId(),
                usuario.getEmail()
        );
    }

    // Fase 4: "Mi Perfil / CV" del postulante autenticado
    public PostulantePerfilResponseDTO obtenerMiPerfil() {
        Usuario usuario = usuarioSecurity.usuarioLogado();

        if (!usuarioSecurity.isPostulante()) {
            throw new BusinessException("Solo los postulantes pueden acceder a este recurso");
        }

        Postulante postulante = postulanteRepository.findByUsuario_Id(usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postulante no encontrado"));

        CurriculumVitae cv = curriculumVitaeRepository.findByUsuario_Id(usuario.getId()).orElse(null);

        return mapearAPerfilDTO(usuario, postulante, cv);
    }

    @Transactional
    public PostulantePerfilResponseDTO actualizarMiPerfil(PostulantePerfilRequestDTO dto) {
        Usuario usuario = usuarioSecurity.usuarioLogado();

        if (!usuarioSecurity.isPostulante()) {
            throw new BusinessException("Solo los postulantes pueden acceder a este recurso");
        }

        Postulante postulante = postulanteRepository.findByUsuario_Id(usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postulante no encontrado"));

        if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
            usuario.setNombre(dto.getNombre());
        }
        if (dto.getApellido() != null && !dto.getApellido().isBlank()) {
            usuario.setApellido(dto.getApellido());
        }
        usuarioRepositorio.save(usuario);

        postulante.setFechaNacimiento(dto.getFechaNacimiento());
        postulante.setGenero(dto.getGenero());
        postulante.setDireccion(dto.getDireccion());
        postulanteRepository.save(postulante);

        // El CV se crea la primera vez que el postulante guarda su perfil (upsert)
        CurriculumVitae cv = curriculumVitaeRepository.findByUsuario_Id(usuario.getId())
                .orElseGet(CurriculumVitae::new);
        cv.setUsuario(usuario);
        cv.setTituloProfesional(dto.getTituloProfesional());
        cv.setDescripcion(dto.getDescripcion());
        cv.setExperiencia(dto.getExperiencia());
        cv.setHabilidades(dto.getHabilidades());
        cv.setEducacion(dto.getEducacion());
        cv.setTelefono(dto.getTelefono());
        cv.setLinkedin(dto.getLinkedin());
        curriculumVitaeRepository.save(cv);

        return mapearAPerfilDTO(usuario, postulante, cv);
    }

    private PostulantePerfilResponseDTO mapearAPerfilDTO(Usuario usuario, Postulante postulante, CurriculumVitae cv) {
        return new PostulantePerfilResponseDTO(
                postulante.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                postulante.getFechaNacimiento(),
                postulante.getGenero(),
                postulante.getDireccion(),
                cv != null ? cv.getTituloProfesional() : null,
                cv != null ? cv.getDescripcion() : null,
                cv != null ? cv.getExperiencia() : null,
                cv != null ? cv.getHabilidades() : null,
                cv != null ? cv.getEducacion() : null,
                cv != null ? cv.getTelefono() : null,
                cv != null ? cv.getLinkedin() : null
        );
    }
}