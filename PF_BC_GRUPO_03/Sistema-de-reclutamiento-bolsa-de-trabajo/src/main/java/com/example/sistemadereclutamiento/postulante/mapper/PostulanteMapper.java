package com.example.sistemadereclutamiento.postulante.mapper;

import com.example.sistemadereclutamiento.postulante.dto.request.PostulanteRequest;
import com.example.sistemadereclutamiento.postulante.entity.Postulante;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostulanteMapper {

    Postulante toEntity(PostulanteRequest postulanteRequest);
}
