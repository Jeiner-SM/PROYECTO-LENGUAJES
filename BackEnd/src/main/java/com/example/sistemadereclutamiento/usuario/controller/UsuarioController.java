package com.example.sistemadereclutamiento.usuario.controller;

import com.example.sistemadereclutamiento.empresa.dto.request.EmpresaRequestDTO;
import com.example.sistemadereclutamiento.empresa.dto.response.EmpresaResponseDTO;
import com.example.sistemadereclutamiento.postulante.dto.request.PostulanteRequest;
import com.example.sistemadereclutamiento.postulante.dto.response.TokenResponsePostulante;

import com.example.sistemadereclutamiento.usuario.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

    @PostMapping("/postulante/register")
    public ResponseEntity<TokenResponsePostulante> registerUsuario(@Valid @RequestBody PostulanteRequest request){
        return ResponseEntity.ok(usuarioService.crearPostulante(request));
    }

    // Registro público de empresas (crea Usuario con rol EMPRESA + Empresa vinculada, estado PENDIENTE).
    @PostMapping("/empresa/register")
    public ResponseEntity<EmpresaResponseDTO> registerEmpresa(@RequestBody @Valid EmpresaRequestDTO request) {
        return new ResponseEntity<>(usuarioService.crearEmpresa(request), HttpStatus.CREATED);
    }

}
