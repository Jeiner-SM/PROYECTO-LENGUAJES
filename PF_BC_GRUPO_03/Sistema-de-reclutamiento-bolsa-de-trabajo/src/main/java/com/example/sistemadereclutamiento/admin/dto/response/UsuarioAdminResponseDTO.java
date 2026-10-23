package com.example.sistemadereclutamiento.admin.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// Representa una fila de la tabla "Gestión de Usuarios" del panel admin.
// Incluye tanto postulantes como empresas, con su rol y nombre de empresa (si aplica).
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioAdminResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String rol;
    private String nombreEmpresa;
    private boolean activo;
    private LocalDateTime fechaCreacion;
}
