package com.example.sistemadereclutamiento.contacto.service;

import com.example.sistemadereclutamiento.contacto.dto.request.MensajeContactoRequestDTO;
import com.example.sistemadereclutamiento.contacto.dto.response.MensajeContactoResponseDTO;
import com.example.sistemadereclutamiento.contacto.entity.MensajeContacto;
import com.example.sistemadereclutamiento.contacto.repository.MensajeContactoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MensajeContactoService {

    private final MensajeContactoRepository mensajeContactoRepository;

    public MensajeContactoResponseDTO guardar(MensajeContactoRequestDTO request) {
        MensajeContacto mensaje = new MensajeContacto();
        mensaje.setNombre(request.nombre());
        mensaje.setEmail(request.email());
        mensaje.setAsunto(request.asunto());
        mensaje.setMensaje(request.mensaje());

        MensajeContacto guardado = mensajeContactoRepository.save(mensaje);

        return toDTO(guardado);
    }

    public Page<MensajeContactoResponseDTO> listar(Pageable pageable) {
        return mensajeContactoRepository.findAllByOrderByFechaEnvioDesc(pageable)
                .map(this::toDTO);
    }

    private MensajeContactoResponseDTO toDTO(MensajeContacto mensaje) {
        return new MensajeContactoResponseDTO(
                mensaje.getId(),
                mensaje.getNombre(),
                mensaje.getEmail(),
                mensaje.getAsunto(),
                mensaje.getMensaje(),
                mensaje.getFechaEnvio()
        );
    }
}
