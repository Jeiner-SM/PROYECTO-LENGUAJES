package com.example.sistemadereclutamiento.usuario.service;


import com.example.sistemadereclutamiento.empresa.dto.request.EmpresaRequestDTO;
import com.example.sistemadereclutamiento.empresa.dto.response.EmpresaResponseDTO;
import com.example.sistemadereclutamiento.empresa.entity.Empresa;
import com.example.sistemadereclutamiento.empresa.entity.EstadoValidacion;
import com.example.sistemadereclutamiento.empresa.mapper.EmpresaMapper;
import com.example.sistemadereclutamiento.empresa.repository.EmpresaRepository;
import com.example.sistemadereclutamiento.postulante.dto.request.PostulanteRequest;
import com.example.sistemadereclutamiento.postulante.dto.response.TokenResponsePostulante;
import com.example.sistemadereclutamiento.postulante.entity.Postulante;
import com.example.sistemadereclutamiento.postulante.mapper.PostulanteMapper;
import com.example.sistemadereclutamiento.postulante.repository.PostulanteRepository;
import com.example.sistemadereclutamiento.rol.entity.Rol;
import com.example.sistemadereclutamiento.rol.repository.RolRepository;
import com.example.sistemadereclutamiento.security.jtw.JwtService;
import com.example.sistemadereclutamiento.shared.exeption.BusinessException;
import com.example.sistemadereclutamiento.shared.exeption.ResourceNotFoundException;
import com.example.sistemadereclutamiento.usuario.dto.request.UsuarioRequestDTO;
import com.example.sistemadereclutamiento.usuario.entity.Usuario;
import com.example.sistemadereclutamiento.usuario.mapper.UsuarioMapper;
import com.example.sistemadereclutamiento.usuario.repository.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepositorio usuarioRepositorio;
    private final RolRepository rolRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final PostulanteMapper postulanteMapper;
    private final PostulanteRepository postulanteRepository;
    private final JwtService jwtService;
    private final UserDetailsService  userDetailsService;
    private final EmpresaMapper empresaMapper;
    private final EmpresaRepository empresaRepository;

    private Usuario crearUsuarioBase(UsuarioRequestDTO usuarioRequestDTO, String nombreRol) {
        Rol rol = rolRepository.findByNombre(nombreRol).orElseThrow(()-> new ResourceNotFoundException("rol no encontrado"));
        if (usuarioRepositorio.findByEmail(usuarioRequestDTO.getEmail()).isPresent()) {
            throw new BusinessException("Ya existe un usuario con ese email");
        }
        Usuario usuario = usuarioMapper.toEntity(usuarioRequestDTO);

        usuario.setPassword(
                passwordEncoder.encode(usuarioRequestDTO.getPassword())
        );

        usuario.setRoles(Set.of(rol));

        return usuarioRepositorio.save(usuario);

    }

    public TokenResponsePostulante crearPostulante(PostulanteRequest request){
        Usuario usuario = crearUsuarioBase(request.usuario(), "POSTULANTE");

        Postulante postulante = postulanteMapper.toEntity(request);
        postulante.setUsuario(usuario);
        postulanteRepository.save(postulante);

        String token = jwtService.generateToken(userDetailsService.loadUserByUsername(usuario.getEmail()));

        return new TokenResponsePostulante(token,"bearer",3600);

    }

    @Transactional
    public EmpresaResponseDTO crearEmpresa(EmpresaRequestDTO request) {
        // Se valida el RUC antes de crear cualquier registro para no dejar un
        // Usuario "huérfano" si la empresa termina siendo rechazada por RUC duplicado.
        if (empresaRepository.existsByRuc(request.getRuc())) {
            throw new BusinessException("Ya existe una empresa registrada con ese RUC");
        }

        Usuario usuario = crearUsuarioBase(request.getUsuario(), "EMPRESA");

        Empresa empresa = empresaMapper.toEntity(request);
        empresa.setUsuario(usuario);
        empresa.setEstadoValidacion(EstadoValidacion.PENDIENTE);
        empresaRepository.save(empresa);

        return empresaMapper.toDTO(empresa);
    }

}
