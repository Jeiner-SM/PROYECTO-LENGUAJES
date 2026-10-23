package com.example.sistemadereclutamiento.oferta.dto.request;

public record OfertaUpdateDTO(String titulo,
                              String descripcion,
                              String ubicacion,
                              Double salario,
                              String modalidad,
                              String estado
                              ) {
}
