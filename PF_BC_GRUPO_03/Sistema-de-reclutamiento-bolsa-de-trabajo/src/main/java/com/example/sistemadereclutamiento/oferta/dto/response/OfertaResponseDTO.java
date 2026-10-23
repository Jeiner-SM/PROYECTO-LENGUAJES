package com.example.sistemadereclutamiento.oferta.dto.response;

import com.example.sistemadereclutamiento.oferta.entity.OfertaEstado;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfertaResponseDTO {

    private Long id;
    private String titulo;
    private String descripcion;
    private String ubicacion;
    private Double salario;
    private String modalidad;
    private String estado;
    private String nombreEmpresa;

}
