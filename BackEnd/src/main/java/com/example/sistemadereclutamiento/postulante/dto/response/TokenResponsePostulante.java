package com.example.sistemadereclutamiento.postulante.dto.response;

public record TokenResponsePostulante(String accessToken,
                                      String tokenType,
                                      long expiresIn) {
}
