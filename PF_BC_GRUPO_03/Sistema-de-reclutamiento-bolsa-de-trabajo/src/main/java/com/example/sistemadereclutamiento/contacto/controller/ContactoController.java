package com.example.sistemadereclutamiento.contacto.controller;

import com.example.sistemadereclutamiento.contacto.dto.request.MensajeContactoRequestDTO;
import com.example.sistemadereclutamiento.contacto.dto.response.MensajeContactoResponseDTO;
import com.example.sistemadereclutamiento.contacto.service.MensajeContactoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Endpoint público: cualquier visitante de la página pública puede enviar un mensaje
// de contacto sin necesidad de autenticarse.
@RestController
@RequestMapping("/api/contacto")
@RequiredArgsConstructor
public class ContactoController {

    private final MensajeContactoService mensajeContactoService;

    @PostMapping
    public ResponseEntity<MensajeContactoResponseDTO> enviarMensaje(@Valid @RequestBody MensajeContactoRequestDTO request) {
        return new ResponseEntity<>(mensajeContactoService.guardar(request), HttpStatus.CREATED);
    }
}
