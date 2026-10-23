package com.example.sistemadereclutamiento.admin.service;

import com.example.sistemadereclutamiento.admin.dto.response.UsuarioAdminResponseDTO;
import com.example.sistemadereclutamiento.empresa.entity.Empresa;
import com.example.sistemadereclutamiento.empresa.repository.EmpresaRepository;
import com.example.sistemadereclutamiento.refreshToken.repository.RefreshTokenRepository;
import com.example.sistemadereclutamiento.rol.entity.Rol;
import com.example.sistemadereclutamiento.shared.exeption.BusinessException;
import com.example.sistemadereclutamiento.shared.exeption.ResourceNotFoundException;
import com.example.sistemadereclutamiento.usuario.entity.Usuario;
import com.example.sistemadereclutamiento.usuario.repository.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUsuarioService {

    private final UsuarioRepositorio usuarioRepositorio;
    private final EmpresaRepository empresaRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public Page<UsuarioAdminResponseDTO> listarUsuarios(String rol, Boolean activo, Pageable pageable) {
        Page<Usuario> pagina = usuarioRepositorio.findAllExceptAdmin(rol, activo, pageable);

        // En vez de consultar la Empresa de cada usuario EMPRESA una por una (N+1),
        // se resuelven todas en una sola query IN (...) y se mapea en memoria.
        List<Long> usuarioIdsEmpresa = pagina.getContent().stream()
                .filter(u -> u.getRoles().stream().anyMatch(r -> "EMPRESA".equalsIgnoreCase(r.getNombre())))
                .map(Usuario::getId)
                .toList();

        Map<Long, String> nombreEmpresaPorUsuarioId = usuarioIdsEmpresa.isEmpty()
                ? Map.of()
                : empresaRepository.findByUsuario_IdIn(usuarioIdsEmpresa).stream()
                        .collect(Collectors.toMap(e -> e.getUsuario().getId(), Empresa::getNombreEmpresa));

        return pagina.map(usuario -> toDTO(usuario, nombreEmpresaPorUsuarioId));
    }

    @Transactional
    public UsuarioAdminResponseDTO cambiarEstado(Long id, boolean activo) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        boolean esAdmin = usuario.getRoles().stream()
                .anyMatch(rol -> "ADMIN".equalsIgnoreCase(rol.getNombre()));
        if (esAdmin) {
            throw new BusinessException("No se puede deshabilitar una cuenta de administrador");
        }

        usuario.setActivo(activo);
        usuarioRepositorio.save(usuario);

        // Al deshabilitar, se revocan sus refresh tokens para cortar la sesión de inmediato
        // (el JwtAuthenticationFilter además rechaza cualquier request con el access token vigente).
        if (!activo) {
            refreshTokenRepository.deleteByUsuario_Id(usuario.getId());
        }

        return toDTO(usuario);
    }

    // Usado solo por cambiarEstado (una sola fila): aquí SÍ es aceptable la consulta
    // individual, ya que no forma parte de un listado paginado.
    private UsuarioAdminResponseDTO toDTO(Usuario usuario) {
        String rol = usuario.getRoles().stream()
                .map(Rol::getNombre)
                .findFirst()
                .orElse("SIN_ROL");

        String nombreEmpresa = null;
        if ("EMPRESA".equalsIgnoreCase(rol)) {
            nombreEmpresa = empresaRepository.findEmpresasByUsuario_Id(usuario.getId())
                    .map(Empresa::getNombreEmpresa)
                    .orElse(null);
        }

        return construirDTO(usuario, rol, nombreEmpresa);
    }

    private UsuarioAdminResponseDTO toDTO(Usuario usuario, Map<Long, String> nombreEmpresaPorUsuarioId) {
        String rol = usuario.getRoles().stream()
                .map(Rol::getNombre)
                .findFirst()
                .orElse("SIN_ROL");

        String nombreEmpresa = "EMPRESA".equalsIgnoreCase(rol)
                ? nombreEmpresaPorUsuarioId.get(usuario.getId())
                : null;

        return construirDTO(usuario, rol, nombreEmpresa);
    }

    private UsuarioAdminResponseDTO construirDTO(Usuario usuario, String rol, String nombreEmpresa) {
        return new UsuarioAdminResponseDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getEmail(),
                rol,
                nombreEmpresa,
                usuario.isActivo(),
                usuario.getFechaCreacion()
        );
    }
}
