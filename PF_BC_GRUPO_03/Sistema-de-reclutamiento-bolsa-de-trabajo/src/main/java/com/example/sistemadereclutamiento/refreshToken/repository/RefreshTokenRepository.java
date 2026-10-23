package com.example.sistemadereclutamiento.refreshToken.repository;


import com.example.sistemadereclutamiento.refreshToken.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);




    void deleteByUsuario_Id(Long id);
}
