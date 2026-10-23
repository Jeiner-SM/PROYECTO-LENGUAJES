package com.example.sistemadereclutamiento.admin.controller;

import com.example.sistemadereclutamiento.contacto.dto.response.MensajeContactoResponseDTO;
import com.example.sistemadereclutamiento.contacto.service.MensajeContactoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Solo un usuario con ROLE_ADMIN puede ver los mensajes enviados desde el
// formulario público de contacto.
@RestController
@RequestMapping("/api/admin/mensajes-contacto")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminMensajeContactoController {

    private final MensajeContactoService mensajeContactoService;

    @GetMapping
    public ResponseEntity<Page<MensajeContactoResponseDTO>> listarMensajes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(mensajeContactoService.listar(pageable));
    }
}
