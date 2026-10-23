package com.example.sistemadereclutamiento.oferta.mapper;


import com.example.sistemadereclutamiento.oferta.dto.request.OfertaRequestDTO;
import com.example.sistemadereclutamiento.oferta.dto.request.OfertaUpdateDTO;
import com.example.sistemadereclutamiento.oferta.dto.response.OfertaResponseDTO;
import com.example.sistemadereclutamiento.oferta.entity.Oferta;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface OfertaMapper {

    @Mapping(source = "empresa.nombreEmpresa", target = "nombreEmpresa")
    OfertaResponseDTO toDTO(Oferta oferta);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "estado", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    Oferta toEntity(OfertaRequestDTO request);

    @BeanMapping(nullValuePropertyMappingStrategy= NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "empresa", ignore = true)
    void updateEntityFromDto(OfertaUpdateDTO dto, @MappingTarget Oferta oferta);
}