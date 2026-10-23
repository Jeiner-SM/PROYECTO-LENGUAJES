package com.example.sistemadereclutamiento.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private long expiresIn;
    private String email;

    private String rol;
    private List<String> permisos;


}