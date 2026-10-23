package com.example.sistemadereclutamiento.config;

import com.example.sistemadereclutamiento.rol.entity.Rol;
import com.example.sistemadereclutamiento.rol.repository.RolRepository;
import com.example.sistemadereclutamiento.shared.exeption.BusinessException;
import com.example.sistemadereclutamiento.shared.exeption.ResourceNotFoundException;
import com.example.sistemadereclutamiento.usuario.entity.Usuario;
import com.example.sistemadereclutamiento.usuario.repository.UsuarioRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataConfig {
    private final UsuarioRepositorio usuarioRepositorio;
    private final RolRepository rolRepository;

    @Bean
    CommandLineRunner init(){
        return args -> {
            if (usuarioRepositorio.findByEmail("AdminWork@gmail.com").isEmpty()){
                Rol rol = rolRepository.findByNombre("ADMIN").orElseThrow(()->new ResourceNotFoundException("Rol no encontrado"));

                Usuario usuario = new Usuario();
                usuario.setNombre("Luis");
                usuario.setApellido("Montiel");
                usuario.setEmail("AdminWork@gmail.com");
                usuario.getRoles().add(rol);
                usuario.setActivo(true);
                usuario.setPassword(new BCryptPasswordEncoder().encode("123456"));
                usuarioRepositorio.save(usuario);
            }
        };
    }
}
