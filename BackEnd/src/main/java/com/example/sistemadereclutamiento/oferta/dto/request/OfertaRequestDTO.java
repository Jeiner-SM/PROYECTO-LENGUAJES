package com.example.sistemadereclutamiento.oferta.dto.request;

import lombok.Data;

@Data
public class OfertaRequestDTO {

    private String titulo;
    private String descripcion;
    private String ubicacion;
    private Double salario;
    private String modalidad;
    private String estado;

}
