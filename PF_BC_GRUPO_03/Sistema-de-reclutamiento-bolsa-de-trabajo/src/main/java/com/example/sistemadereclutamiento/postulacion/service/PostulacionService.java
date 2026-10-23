package com.example.sistemadereclutamiento.postulacion.service;

import com.example.sistemadereclutamiento.postulacion.dto.request.PostulacionRequestDTO;
import com.example.sistemadereclutamiento.postulacion.dto.request.PostulacionUpdateDTO;
import com.example.sistemadereclutamiento.postulacion.dto.response.PostulacionResponseDTO;
import com.example.sistemadereclutamiento.postulacion.entity.EstadoPostulacion;

import java.util.List;

public interface PostulacionService {
    PostulacionResponseDTO crearPostulacion(PostulacionRequestDTO dto);
    PostulacionResponseDTO actualizarPostulacion(Long id, PostulacionUpdateDTO dto);
    List<PostulacionResponseDTO> obtenerPorPostulante(Long postulanteId);
    List<PostulacionResponseDTO> obtenerPorOferta(Long ofertaId);
    PostulacionResponseDTO obtenerDetalle(Long id);
    List<PostulacionResponseDTO> obtenerPorEstadoYEmpresa(EstadoPostulacion estado, Long empresaId);

    // Fase 3: listado propio y anulación por parte del postulante autenticado
    List<PostulacionResponseDTO> misPostulaciones();
    PostulacionResponseDTO cancelarPostulacion(Long id);
}