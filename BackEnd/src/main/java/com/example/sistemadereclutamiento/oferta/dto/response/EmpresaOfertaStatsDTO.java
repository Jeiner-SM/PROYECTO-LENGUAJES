package com.example.sistemadereclutamiento.oferta.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmpresaOfertaStatsDTO {
    private long totalOfertas;
    private long activas;
    private long cerradas;
}
