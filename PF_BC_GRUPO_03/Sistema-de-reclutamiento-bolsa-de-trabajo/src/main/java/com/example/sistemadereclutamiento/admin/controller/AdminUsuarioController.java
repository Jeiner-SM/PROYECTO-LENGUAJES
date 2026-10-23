package com.example.sistemadereclutamiento.admin.controller;

import com.example.sistemadereclutamiento.admin.dto.request.EstadoUsuarioUpdateDTO;
import com.example.sistemadereclutamiento.admin.dto.response.UsuarioAdminResponseDTO;
import com.example.sistemadereclutamiento.admin.service.AdminUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

// Todos los endpoints de este controlador son exclusivos del panel de administración:
// solo un usuario con ROLE_ADMIN puede acceder (verificado a nivel de clase).
@RestController
@RequestMapping("/api/admin/usuarios")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUsuarioController {

    private final AdminUsuarioService adminUsuarioService;

    @GetMapping
    public ResponseEntity<Page<UsuarioAdminResponseDTO>> listarUsuarios(
            @RequestParam(required = false) String rol,
            @RequestParam(required = false) Boolean activo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(adminUsuarioService.listarUsuarios(rol, activo, pageable));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<UsuarioAdminResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestBody EstadoUsuarioUpdateDTO request
    ) {
        return ResponseEntity.ok(adminUsuarioService.cambiarEstado(id, request.activo()));
    }
}
