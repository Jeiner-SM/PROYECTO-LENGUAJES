package com.example.sistemadereclutamiento.admin.dto.response;

import com.example.sistemadereclutamiento.empresa.entity.EstadoValidacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Representa una fila de la pestaña "Empresas" en "Gestión de Usuarios" del panel admin.
// "id" es el id de la Empresa (usado para verificar), "usuarioId" es el id del Usuario
// asociado (usado para habilitar/deshabilitar la cuenta).
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaAdminResponseDTO {
    private Long id;
    private Long usuarioId;
    private String nombreEmpresa;
    private String ruc;
    private String email;
    private EstadoValidacion estadoValidacion;
    private boolean activo;
    private LocalDateTime fechaCreacion;
}
