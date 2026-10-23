package com.example.sistemadereclutamiento.admin.service;

import com.example.sistemadereclutamiento.admin.dto.response.EmpresaAdminResponseDTO;
import com.example.sistemadereclutamiento.empresa.entity.Empresa;
import com.example.sistemadereclutamiento.empresa.entity.EstadoValidacion;
import com.example.sistemadereclutamiento.empresa.repository.EmpresaRepository;
import com.example.sistemadereclutamiento.shared.exeption.ResourceNotFoundException;
import com.example.sistemadereclutamiento.usuario.entity.Usuario;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminEmpresaService {

    private final EmpresaRepository empresaRepository;

    public Page<EmpresaAdminResponseDTO> listarEmpresas(Boolean activo, EstadoValidacion estadoValidacion, Pageable pageable) {
        return empresaRepository.findAllForAdmin(activo, estadoValidacion, pageable).map(this::toDTO);
    }

    public long contarPendientes() {
        return empresaRepository.countByEstadoValidacion(EstadoValidacion.PENDIENTE);
    }

    @Transactional
    public EmpresaAdminResponseDTO verificarEmpresa(Long empresaId) {
        Empresa empresa = empresaRepository.findById(empresaId)
                .orElseThrow(() -> new ResourceNotFoundException("Empresa no encontrada"));

        empresa.setEstadoValidacion(EstadoValidacion.ACTIVO);
        empresaRepository.save(empresa);

        return toDTO(empresa);
    }

    private EmpresaAdminResponseDTO toDTO(Empresa empresa) {
        Usuario usuario = empresa.getUsuario();

        return new EmpresaAdminResponseDTO(
                empresa.getId(),
                usuario.getId(),
                empresa.getNombreEmpresa(),
                empresa.getRuc(),
                usuario.getEmail(),
                empresa.getEstadoValidacion(),
                usuario.isActivo(),
                usuario.getFechaCreacion()
        );
    }
}
