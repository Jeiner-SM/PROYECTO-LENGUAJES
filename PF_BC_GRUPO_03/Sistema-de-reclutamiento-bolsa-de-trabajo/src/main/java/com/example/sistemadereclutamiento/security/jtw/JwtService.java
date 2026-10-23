package com.example.sistemadereclutamiento.security.jtw;

import com.example.sistemadereclutamiento.empresa.entity.Empresa;
import com.example.sistemadereclutamiento.empresa.repository.EmpresaRepository;
import com.example.sistemadereclutamiento.security.service.CustomUserDetail;
import com.example.sistemadereclutamiento.usuario.entity.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {

    private final JwtProperties jwtProperties;
    private final EmpresaRepository empresaRepository;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateToken(UserDetails user) {

        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).filter(
                        auth -> auth.startsWith("ROLE_")
                ).toList();
        List<String> permisos = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).filter(
                        auth -> !auth.startsWith("ROLE_")
                ).toList();
        return
                Jwts.builder().
                        subject(user.getUsername())
                        .claim("roles", roles)
                        .claim("permisos", permisos)
                        .claim("nombre", resolverNombreVisible(user))

                        .issuedAt(new Date())
                        .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration())).
                        signWith(getSigningKey())
                        .compact();
    }

    // El nombre visible en la UI (sidebar) es el nombre comercial de la empresa
    // cuando el usuario tiene rol EMPRESA, o su nombre completo en cualquier otro caso.
    private String resolverNombreVisible(UserDetails user) {
        if (!(user instanceof CustomUserDetail customUserDetail)) {
            return user.getUsername();
        }

        Usuario usuario = customUserDetail.getUsuario();

        boolean esEmpresa = usuario.getRoles().stream()
                .anyMatch(rol -> "EMPRESA".equalsIgnoreCase(rol.getNombre()));

        if (esEmpresa) {
            String nombreEmpresa = empresaRepository.findEmpresasByUsuario_Id(usuario.getId())
                    .map(Empresa::getNombreEmpresa)
                    .orElse(null);
            if (nombreEmpresa != null && !nombreEmpresa.isBlank()) {
                return nombreEmpresa;
            }
        }

        String nombreCompleto = ((usuario.getNombre() != null ? usuario.getNombre() : "") + " " +
                (usuario.getApellido() != null ? usuario.getApellido() : "")).trim();

        return nombreCompleto.isBlank() ? usuario.getEmail() : nombreCompleto;
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);


            return claims.getExpiration().after(new Date());

        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT inválido: {}", e.getMessage());
            return false;
        }
    }


}
