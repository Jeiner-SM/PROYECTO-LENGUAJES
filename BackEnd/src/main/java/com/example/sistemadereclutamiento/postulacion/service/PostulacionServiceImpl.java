package com.example.sistemadereclutamiento.postulacion.service;

import com.example.sistemadereclutamiento.curriculum.entity.CurriculumVitae;
import com.example.sistemadereclutamiento.curriculum.repository.CurriculumVitaeRepository;
import com.example.sistemadereclutamiento.oferta.entity.Oferta;
import com.example.sistemadereclutamiento.oferta.repository.OfertaRepository;
import com.example.sistemadereclutamiento.postulacion.dto.request.PostulacionRequestDTO;
import com.example.sistemadereclutamiento.postulacion.dto.request.PostulacionUpdateDTO;
import com.example.sistemadereclutamiento.postulacion.dto.response.PostulacionResponseDTO;
import com.example.sistemadereclutamiento.postulacion.entity.EstadoPostulacion;
import com.example.sistemadereclutamiento.postulacion.entity.Postulacion;
import com.example.sistemadereclutamiento.postulacion.repository.PostulacionRepository;
import com.example.sistemadereclutamiento.postulante.entity.Postulante;
import com.example.sistemadereclutamiento.postulante.repository.PostulanteRepository;
import com.example.sistemadereclutamiento.shared.exeption.BusinessException;
import com.example.sistemadereclutamiento.shared.exeption.ResourceNotFoundException;
import com.example.sistemadereclutamiento.usuario.entity.Usuario;
import com.example.sistemadereclutamiento.usuario.service.UsuarioSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostulacionServiceImpl implements PostulacionService {

    @Autowired
    private PostulacionRepository postulacionRepository;

    @Autowired
    private OfertaRepository ofertaRepository;

    @Autowired
    private PostulanteRepository postulanteRepository;

    @Autowired
    private UsuarioSecurity usuarioSecurity;

    @Autowired
    private CurriculumVitaeRepository curriculumVitaeRepository;

    @Override
    @Transactional // Aporta a la rúbrica de transaccionalidad
    public PostulacionResponseDTO crearPostulacion(PostulacionRequestDTO dto) {
        Optional<Postulacion> postulacionExistente = postulacionRepository
                .findByPostulanteIdAndOfertaId(dto.getPostulanteId(), dto.getOfertaId());

        // Re-postulación: si la postulación previa fue CANCELADA, se reutiliza el
        // mismo registro en vez de bloquear al postulante o crear un duplicado.
        if (postulacionExistente.isPresent()) {
            Postulacion postulacion = postulacionExistente.get();

            if (postulacion.getEstado() != EstadoPostulacion.CANCELADO) {
                throw new BusinessException("El postulante ya aplicó a esta oferta laboral");
            }

            postulacion.setCvUrl(dto.getCvUrl());
            postulacion.setEstado(EstadoPostulacion.ENVIADO);
            postulacion.setFechaPostulacion(LocalDateTime.now());

            return mapearADto(postulacionRepository.save(postulacion));
        }

        Postulante postulante = postulanteRepository.findById(dto.getPostulanteId())
                .orElseThrow(() -> new ResourceNotFoundException("Postulante no encontrado"));
        
        Oferta oferta = ofertaRepository.findById(dto.getOfertaId())
                .orElseThrow(() -> new ResourceNotFoundException("Oferta no encontrada"));

        Postulacion postulacion = new Postulacion();
        postulacion.setPostulante(postulante);
        postulacion.setOferta(oferta);
        postulacion.setCvUrl(dto.getCvUrl());
        postulacion.setEstado(EstadoPostulacion.ENVIADO);
        
        Postulacion guardada = postulacionRepository.save(postulacion);
        
        return mapearADto(guardada);
    }

    @Override
    @Transactional
    public PostulacionResponseDTO actualizarPostulacion(Long id, PostulacionUpdateDTO dto) {
        Postulacion postulacion = postulacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Postulación no encontrada"));

        // Solo la empresa dueña de la oferta (o un admin) puede cambiar el estado de una postulación.
        validarOfertaPropiaDeLaEmpresa(postulacion.getOferta());

        if (dto.getEstado() != null) {
            postulacion.setEstado(EstadoPostulacion.valueOf(dto.getEstado().toUpperCase()));
        }
        if (dto.getCvUrl() != null) {
            postulacion.setCvUrl(dto.getCvUrl());
        }

        Postulacion actualizada = postulacionRepository.save(postulacion);
        return mapearADto(actualizada);
    }

    @Override
    public List<PostulacionResponseDTO> obtenerPorPostulante(Long postulanteId) {
        return mapearListaADto(postulacionRepository.findByPostulanteId(postulanteId));
    }

    @Override
    public List<PostulacionResponseDTO> obtenerPorOferta(Long ofertaId) {
        Oferta oferta = ofertaRepository.findById(ofertaId)
                .orElseThrow(() -> new ResourceNotFoundException("Oferta no encontrada"));

        // Evita que una empresa vea los postulantes (nombre, email, CV) de una oferta ajena.
        validarOfertaPropiaDeLaEmpresa(oferta);

        return mapearListaADto(postulacionRepository.findByOfertaId(ofertaId));
    }

    @Override
    public PostulacionResponseDTO obtenerDetalle(Long id) {
        Postulacion postulacion = postulacionRepository.findByIdDetailed(id)
                .orElseThrow(() -> new ResourceNotFoundException("Postulación no encontrada"));
        return mapearADto(postulacion);
    }

    @Override
    public List<PostulacionResponseDTO> obtenerPorEstadoYEmpresa(EstadoPostulacion estado, Long empresaId) {
        return mapearListaADto(postulacionRepository.findByEstadoAndOfertaEmpresaId(estado, empresaId));
    }

    @Override
    public List<PostulacionResponseDTO> misPostulaciones() {
        Postulante postulante = obtenerPostulanteActual();
        return obtenerPorPostulante(postulante.getId());
    }

    @Override
    @Transactional
    public PostulacionResponseDTO cancelarPostulacion(Long id) {
        Postulacion postulacion = postulacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Postulación no encontrada"));

        Postulante postulante = obtenerPostulanteActual();

        boolean esPropia = postulacion.getPostulante().getId().equals(postulante.getId());
        if (!esPropia && !usuarioSecurity.isAdmin()) {
            throw new BusinessException("No tienes permiso para cancelar esta postulación");
        }

        if (postulacion.getEstado() == EstadoPostulacion.CANCELADO) {
            throw new BusinessException("Esta postulación ya fue cancelada");
        }

        if (postulacion.getEstado() == EstadoPostulacion.ACEPTADO || postulacion.getEstado() == EstadoPostulacion.RECHAZADO) {
            throw new BusinessException("No se puede cancelar una postulación que ya fue finalizada");
        }

        postulacion.setEstado(EstadoPostulacion.CANCELADO);
        Postulacion actualizada = postulacionRepository.save(postulacion);
        return mapearADto(actualizada);
    }

    private Postulante obtenerPostulanteActual() {
        Usuario usuario = usuarioSecurity.usuarioLogado();
        return postulanteRepository.findByUsuario_Id(usuario.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Postulante no encontrado"));
    }

    // Solo la empresa dueña de la oferta (o un admin) puede ver/gestionar sus postulaciones.
    private void validarOfertaPropiaDeLaEmpresa(Oferta oferta) {
        if (usuarioSecurity.isAdmin()) {
            return;
        }

        if (!usuarioSecurity.isEmpresa()) {
            throw new BusinessException("Solo las empresas pueden acceder a este recurso");
        }

        Usuario usuarioLogueado = usuarioSecurity.usuarioLogado();
        Long propietarioId = oferta.getEmpresa().getUsuario().getId();

        if (!propietarioId.equals(usuarioLogueado.getId())) {
            throw new AccessDeniedException("No tienes permiso para gestionar los postulantes de esta oferta");
        }
    }

    // Mapea una lista completa de postulaciones haciendo UNA sola query batch para
    // resolver los CV de todos los postulantes involucrados (en vez de 1 query por fila,
    // como ocurría antes al llamar curriculumVitaeRepository.findByUsuario_Id por cada una).
    private List<PostulacionResponseDTO> mapearListaADto(List<Postulacion> postulaciones) {
        if (postulaciones.isEmpty()) {
            return List.of();
        }

        List<Long> usuarioIds = postulaciones.stream()
                .map(p -> p.getPostulante().getUsuario().getId())
                .distinct()
                .toList();

        Map<Long, Long> curriculumIdPorUsuarioId = curriculumVitaeRepository.findByUsuario_IdIn(usuarioIds).stream()
                .collect(Collectors.toMap(cv -> cv.getUsuario().getId(), CurriculumVitae::getId));

        return postulaciones.stream()
                .map(p -> mapearADto(p, curriculumIdPorUsuarioId))
                .collect(Collectors.toList());
    }

    private PostulacionResponseDTO mapearADto(Postulacion p) {
        Usuario usuarioPostulante = p.getPostulante().getUsuario();
        Long curriculumId = curriculumVitaeRepository.findByUsuario_Id(usuarioPostulante.getId())
                .map(CurriculumVitae::getId)
                .orElse(null);
        // Collections.singletonMap (a diferencia de Map.of) admite un valor null.
        return mapearADto(p, java.util.Collections.singletonMap(usuarioPostulante.getId(), curriculumId));
    }

    private PostulacionResponseDTO mapearADto(Postulacion p, Map<Long, Long> curriculumIdPorUsuarioId) {
        PostulacionResponseDTO dto = new PostulacionResponseDTO();
        dto.setId(p.getId());
        dto.setPostulanteId(p.getPostulante().getId());
        Usuario usuarioPostulante = p.getPostulante().getUsuario();
        dto.setNombrePostulante(
                (usuarioPostulante.getNombre() != null ? usuarioPostulante.getNombre() : "") +
                        " " +
                        (usuarioPostulante.getApellido() != null ? usuarioPostulante.getApellido() : "")
        );
        dto.setEmailPostulante(usuarioPostulante.getEmail());
        dto.setOfertaId(p.getOferta().getId());
        dto.setOfertaTitulo(p.getOferta().getTitulo());
        dto.setNombreEmpresa(p.getOferta().getEmpresa().getNombreEmpresa());
        dto.setCvUrl(p.getCvUrl());
        // Si el postulante llenó su "Mi Perfil / CV", enlazamos ese currículum
        // estructurado para que la empresa pueda consultarlo aunque no haya cvUrl.
        Long curriculumId = curriculumIdPorUsuarioId.get(usuarioPostulante.getId());
        if (curriculumId != null) {
            dto.setCurriculumId(curriculumId);
        }
        dto.setEstado(p.getEstado().name());
        dto.setFechaPostulacion(p.getFechaPostulacion());
        return dto;
    }
}