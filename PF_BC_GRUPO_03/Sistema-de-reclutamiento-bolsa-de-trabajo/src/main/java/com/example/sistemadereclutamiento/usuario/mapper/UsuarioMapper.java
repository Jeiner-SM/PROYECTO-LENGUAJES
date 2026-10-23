package com.example.sistemadereclutamiento.usuario.mapper;


import com.example.sistemadereclutamiento.usuario.dto.request.UsuarioRequestDTO;
import com.example.sistemadereclutamiento.usuario.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



@Mapper(componentModel = "spring")
public interface UsuarioMapper {
    @Mapping(target = "roles", ignore = true)
    Usuario toEntity(UsuarioRequestDTO dto);
    UsuarioRequestDTO toDTO(Usuario entity);


}
