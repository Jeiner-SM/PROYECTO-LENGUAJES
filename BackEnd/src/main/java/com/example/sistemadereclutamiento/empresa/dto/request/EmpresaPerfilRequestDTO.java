package com.example.sistemadereclutamiento.empresa.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// No incluye RUC ni estadoValidacion: son datos legales/de auditoría que la
// propia empresa no debe poder autoeditar desde su perfil.
@Data
public class EmpresaPerfilRequestDTO {

    @NotBlank(message = "El nombre de la empresa es obligatorio")
    private String nombreEmpresa;

    @NotBlank(message = "La razón social es obligatoria")
    private String razonSocial;

    @Size(max = 500, message = "La descripción no debe superar los 500 caracteres")
    private String descripcion;

    @Size(max = 200, message = "La dirección no debe superar los 200 caracteres")
    private String direccion;

    @Size(max = 150, message = "La página web no debe superar los 150 caracteres")
    private String paginaWeb;
}
