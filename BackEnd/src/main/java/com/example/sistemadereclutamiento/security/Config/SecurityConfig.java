package com.example.sistemadereclutamiento.security.Config;


import com.example.sistemadereclutamiento.security.filter.JwtAuthenticationFilter;
import com.example.sistemadereclutamiento.security.service.CustomUserDetailsService;
import com.example.sistemadereclutamiento.shared.exeption.JwtAccesDeniedHandler;
import com.example.sistemadereclutamiento.shared.exeption.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccesDeniedHandler jwtAccesDeniedHandler;
    private final CustomUserDetailsService UserDetailService;
    private final JwtAuthenticationFilter filter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())

                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                .accessDeniedHandler(jwtAccesDeniedHandler)
                )

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .userDetailsService(UserDetailService)

                .authorizeHttpRequests(auth -> auth


                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/uploads/**"
                        ).permitAll()


                        .requestMatchers(
                                "/api/usuarios/postulante/register",
                                "/api/usuarios/empresa/register"
                        ).permitAll()


                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/ofertas/public/**"
                        ).permitAll()


                        // Catálogo público de empresas (home, listado de empresas en el frontend)
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/empresas"
                        ).permitAll()


                        // Formulario público de contacto (footer / página /contacto del frontend)
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/contacto"
                        ).permitAll()


                        .anyRequest().authenticated()
                )

                .addFilterBefore(
                        filter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return  new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS"));

        config.setAllowedHeaders(List.of(
                "Authorization",
                "Content-Type",
                "Accept"
        ));

        config.setExposedHeaders(List.of("Authorization"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
