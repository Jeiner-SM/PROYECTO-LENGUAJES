package com.example.sistemadereclutamiento.usuario.service;


import com.example.sistemadereclutamiento.security.service.CustomUserDetail;
import com.example.sistemadereclutamiento.security.service.CustomUserDetailsService;
import com.example.sistemadereclutamiento.shared.exeption.ResourceNotFoundException;
import com.example.sistemadereclutamiento.usuario.entity.Usuario;
import com.example.sistemadereclutamiento.usuario.repository.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsuarioSecurity {
    private final UsuarioRepositorio usuarioRepositorio;

    public Usuario usuarioLogado() {
        String email =  SecurityContextHolder.getContext().getAuthentication().getName();
        return usuarioRepositorio.findByEmail(email).orElseThrow(()->new ResourceNotFoundException("Usuario no encontrado")) ;
    }

    public boolean isAdmin(){
       return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
    public boolean isEmpresa(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_EMPRESA"));
    }

    public boolean isPostulante(){
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_POSTULANTE"));
    }
}
