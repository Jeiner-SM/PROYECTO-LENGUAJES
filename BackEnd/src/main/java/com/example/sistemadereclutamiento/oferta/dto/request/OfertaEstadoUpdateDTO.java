package com.example.sistemadereclutamiento.oferta.dto.request;

// DTO exclusivo para el cambio rápido de estado de una oferta
// (ej. desde el switch/dropdown de la tabla de "Gestionar Ofertas").
public record OfertaEstadoUpdateDTO(String estado) {
}
