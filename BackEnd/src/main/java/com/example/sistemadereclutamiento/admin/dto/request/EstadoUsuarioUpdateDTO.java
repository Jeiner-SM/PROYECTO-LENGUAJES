package com.example.sistemadereclutamiento.admin.dto.request;

// DTO exclusivo para el switch/botón de Habilitar-Deshabilitar cuenta
// en la tabla de "Gestión de Usuarios" del panel admin.
public record EstadoUsuarioUpdateDTO(boolean activo) {
}
