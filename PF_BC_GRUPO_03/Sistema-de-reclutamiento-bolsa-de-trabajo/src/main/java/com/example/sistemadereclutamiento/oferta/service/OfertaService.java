package com.example.sistemadereclutamiento.oferta.service;

import com.example.sistemadereclutamiento.oferta.dto.request.OfertaEstadoUpdateDTO;
import com.example.sistemadereclutamiento.oferta.dto.request.OfertaUpdateDTO;
import com.example.sistemadereclutamiento.oferta.dto.response.EmpresaOfertaStatsDTO;
import com.example.sistemadereclutamiento.oferta.entity.OfertaEstado;
import com.example.sistemadereclutamiento.oferta.mapper.OfertaMapper;
import com.example.sistemadereclutamiento.shared.exeption.BusinessException;
import com.example.sistemadereclutamiento.usuario.entity.Usuario;
import com.example.sistemadereclutamiento.usuario.service.UsuarioSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.sistemadereclutamiento.oferta.dto.request.OfertaRequestDTO;
import com.example.sistemadereclutamiento.oferta.dto.response.OfertaResponseDTO;
import com.example.sistemadereclutamiento.shared.exeption.ResourceNotFoundException;
import com.example.sistemadereclutamiento.empresa.entity.Empresa;
import com.example.sistemadereclutamiento.oferta.entity.Oferta;
import com.example.sistemadereclutamiento.empresa.repository.EmpresaRepository;
import com.example.sistemadereclutamiento.oferta.repository.OfertaRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OfertaService {

    private final OfertaRepository ofertaRepository;
    private final EmpresaRepository empresaRepository;
    private final OfertaMapper ofertaMapper;
    private final UsuarioSecurity usuarioSecurity;

    public OfertaResponseDTO guardarOferta(OfertaRequestDTO requestDTO) {

        Usuario usuarioLogueado = usuarioSecurity.usuarioLogado();

        Empresa empresa = empresaRepository.findEmpresasByUsuario_Id(usuarioLogueado.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usted no tiene ninguna empresa registrada"));

        if (!usuarioSecurity.isEmpresa()) {
            throw new BusinessException("solo empresas pueden crear ofertas");
        }

        Oferta oferta = ofertaMapper.toEntity(requestDTO);
        oferta.setEmpresa(empresa);
        oferta.setEstado(OfertaEstado.ACTIVA);

        Oferta nuevaOferta = ofertaRepository.save(oferta);

        return ofertaMapper.toDTO(nuevaOferta);
    }

    public Page<OfertaResponseDTO> obtenerOfertasAdmin(Pageable pageable) {
        return ofertaRepository.listarOfertas(pageable);
    }

    public Page<OfertaResponseDTO> obtenerOfertasEmpresa(Pageable pageable) {
        Usuario usuarioLogueado = usuarioSecurity.usuarioLogado();
        return ofertaRepository.listarOfertasEmpresa(pageable, usuarioLogueado.getId());
    }

    public Page<OfertaResponseDTO> obtenerOfertasPublicas(Pageable pageable) {
        return ofertaRepository.listarOfertasActivas(pageable);
    }

    public OfertaResponseDTO obtenerOfertaPublicaPorId(Long id) {
        return ofertaRepository.findActivaById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró la oferta activa con ID: " + id));
    }

    public OfertaResponseDTO obtenerPorId(Long id) {
        Oferta oferta = ofertaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró la oferta con ID: " + id));

        return ofertaMapper.toDTO(oferta);
    }

    // Usado por la empresa para precargar el formulario de edición: solo puede
    // ver el detalle completo (incluso si no está ACTIVA) de sus PROPIAS ofertas.
    public OfertaResponseDTO obtenerOfertaEmpresaPorId(Long id) {
        Oferta oferta = ofertaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró la oferta con ID: " + id));

        validarPropietario(oferta);

        return ofertaMapper.toDTO(oferta);
    }

    public EmpresaOfertaStatsDTO obtenerEstadisticasEmpresa() {
        Usuario usuarioLogueado = usuarioSecurity.usuarioLogado();

        // 1 sola consulta con 3 COUNT(CASE WHEN ...) en vez de 3 queries separadas.
        return ofertaRepository.obtenerEstadisticasPorUsuario(usuarioLogueado.getId());
    }

    public OfertaResponseDTO actualizarOferta(Long id, OfertaUpdateDTO requestDTO) {

        Oferta oferta = ofertaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró la oferta con ID: " + id));

        validarPropietario(oferta);

        if (oferta.getEstado() == OfertaEstado.CERRADA || oferta.getEstado() == OfertaEstado.ELIMINADA) {
            throw new BusinessException("esta oferta ya no se puede modificar");
        }

        ofertaMapper.updateEntityFromDto(requestDTO, oferta);

        return ofertaMapper.toDTO(ofertaRepository.save(oferta));
    }

    // Usado por el switch/dropdown de "Gestionar Ofertas" para cambiar el estado
    // de una vacante (ej. de ACTIVA a CERRADA) sin tocar el resto de sus datos.
    public OfertaResponseDTO cambiarEstadoOferta(Long id, OfertaEstadoUpdateDTO requestDTO) {

        Oferta oferta = ofertaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró la oferta con ID: " + id));

        validarPropietario(oferta);

        if (oferta.getEstado() == OfertaEstado.ELIMINADA) {
            throw new BusinessException("Esta oferta fue eliminada y no se puede modificar su estado");
        }

        OfertaEstado nuevoEstado = parsearEstado(requestDTO.estado());

        if (nuevoEstado == OfertaEstado.ELIMINADA) {
            throw new BusinessException("Para eliminar una oferta utiliza la acción de eliminar");
        }

        oferta.setEstado(nuevoEstado);

        return ofertaMapper.toDTO(ofertaRepository.save(oferta));
    }

    private OfertaEstado parsearEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            throw new BusinessException("El estado es obligatorio");
        }

        try {
            return OfertaEstado.valueOf(estado.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Estado inválido: " + estado);
        }
    }

    public void eliminarOferta(Long id) {
        Oferta oferta = ofertaRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No se encontró la oferta con ID: " + id));

        validarPropietario(oferta);

        ofertaRepository.delete(oferta);
    }

    // Evita que una empresa autenticada pueda editar/eliminar/consultar en detalle
    // ofertas que pertenecen a OTRA empresa (antes no se validaba en absoluto).
    private void validarPropietario(Oferta oferta) {
        if (usuarioSecurity.isAdmin()) {
            return;
        }

        Usuario usuarioLogueado = usuarioSecurity.usuarioLogado();
        Long propietarioId = oferta.getEmpresa().getUsuario().getId();

        if (!propietarioId.equals(usuarioLogueado.getId())) {
            throw new AccessDeniedException("No tienes permiso para gestionar esta oferta");
        }
    }
}