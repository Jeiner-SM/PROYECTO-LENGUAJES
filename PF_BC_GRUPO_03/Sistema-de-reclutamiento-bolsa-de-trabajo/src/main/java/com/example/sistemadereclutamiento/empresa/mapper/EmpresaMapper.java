package com.example.sistemadereclutamiento.empresa.mapper;

import com.example.sistemadereclutamiento.empresa.dto.request.EmpresaRequestDTO;
import com.example.sistemadereclutamiento.empresa.dto.response.EmpresaResponseDTO;
import com.example.sistemadereclutamiento.empresa.entity.Empresa;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmpresaMapper {
    @Mapping(target = "id" , ignore = true)
    @Mapping(target = "estadoValidacion", ignore = true)
    Empresa toEntity(EmpresaRequestDTO empresaDTO);


    @Mapping(target = "usuarioEmail", source = "usuario.email")
    EmpresaResponseDTO toDTO(Empresa empresa);


    void updateEntityFromDto(EmpresaRequestDTO dto, @MappingTarget Empresa entity);

}
