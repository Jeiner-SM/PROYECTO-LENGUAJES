package com.example.sistemadereclutamiento.refreshToken.service;

import com.example.sistemadereclutamiento.refreshToken.entity.RefreshToken;
import com.example.sistemadereclutamiento.refreshToken.repository.RefreshTokenRepository;


import com.example.sistemadereclutamiento.shared.exeption.BusinessException;
import com.example.sistemadereclutamiento.usuario.entity.Usuario;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken crearRefreshToken(Usuario usuario) {
        RefreshToken token = new RefreshToken();
        token.setUsuario(usuario);
        token.setToken(UUID.randomUUID().toString());
        token.setExpiryDate(LocalDateTime.now().plusDays(7));
        return refreshTokenRepository.save(token);

    }

    public RefreshToken validarRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token).orElseThrow(()->new BusinessException("refresh  token no existe"));

        if (refreshToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BusinessException("refresh token expiry");
        }

        if (refreshToken.isRevoked()){
            throw new BusinessException("refresh token revoked");
        }
        return refreshToken;
    }

    public void RevokedAllUserTokens(Long userId){
        refreshTokenRepository.deleteByUsuario_Id(userId);
    }
}