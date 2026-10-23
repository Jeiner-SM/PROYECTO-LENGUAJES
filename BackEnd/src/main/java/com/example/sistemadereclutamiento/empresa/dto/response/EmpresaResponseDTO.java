package com.example.sistemadereclutamiento.empresa.dto.response;

import com.example.sistemadereclutamiento.empresa.entity.EstadoValidacion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaResponseDTO {
    private Long id;
    private String nombreEmpresa;
    private String razonSocial;
    private String ruc;
    private EstadoValidacion estadoValidacion;
    private String usuarioEmail; 
}