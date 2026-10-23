package com.example.sistemadereclutamiento.admin.controller;

import com.example.sistemadereclutamiento.admin.dto.response.EmpresaAdminResponseDTO;
import com.example.sistemadereclutamiento.admin.service.AdminEmpresaService;
import com.example.sistemadereclutamiento.empresa.entity.EstadoValidacion;
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
@RequestMapping("/api/admin/empresas")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminEmpresaController {

    private final AdminEmpresaService adminEmpresaService;

    @GetMapping
    public ResponseEntity<Page<EmpresaAdminResponseDTO>> listarEmpresas(
            @RequestParam(required = false) Boolean activo,
            @RequestParam(required = false) EstadoValidacion estadoValidacion,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(adminEmpresaService.listarEmpresas(activo, estadoValidacion, pageable));
    }

    @GetMapping("/pendientes/count")
    public ResponseEntity<Long> contarPendientes() {
        return ResponseEntity.ok(adminEmpresaService.contarPendientes());
    }

    @PatchMapping("/{id}/verificar")
    public ResponseEntity<EmpresaAdminResponseDTO> verificarEmpresa(@PathVariable Long id) {
        return ResponseEntity.ok(adminEmpresaService.verificarEmpresa(id));
    }
}
