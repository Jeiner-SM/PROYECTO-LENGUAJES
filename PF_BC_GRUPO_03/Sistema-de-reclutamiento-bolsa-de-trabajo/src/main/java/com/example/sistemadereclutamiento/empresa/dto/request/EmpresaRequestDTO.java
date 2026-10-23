package com.example.sistemadereclutamiento.empresa.dto.request;

import com.example.sistemadereclutamiento.usuario.dto.request.UsuarioRequestDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmpresaRequestDTO {
    @NotBlank(message = "El nombre de la empresa es obligatorio")
    private String nombreEmpresa;

    @NotBlank(message = "El RUC es obligatorio")

    private String ruc;

    @Size(max = 500, message = "La descripción no debe superar los 500 caracteres")
    private String descripcion;

    @Size(max = 200, message = "La dirección no debe superar los 200 caracteres")
    private String direccion;

    @NotBlank(message = "La razón social es obligatoria")
    private String razonSocial;

    @Size(max = 150, message = "La página web no debe superar los 150 caracteres")
    private String paginaWeb;

    @NotNull(message = "El usuario es obligatorio")
    @Valid
    private UsuarioRequestDTO usuario;
}