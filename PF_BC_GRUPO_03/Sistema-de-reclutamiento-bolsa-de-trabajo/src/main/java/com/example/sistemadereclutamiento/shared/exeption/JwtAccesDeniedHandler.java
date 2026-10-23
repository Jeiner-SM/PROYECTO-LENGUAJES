package com.example.sistemadereclutamiento.shared.exeption;


import com.example.sistemadereclutamiento.shared.response.ApiErrorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAccesDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        ApiErrorDTO error = new ApiErrorDTO(
                "AUTH_FORBIDDEN",
                "No tienes permisos para esta acción",
                LocalDateTime.now().toString()
        );

        response.getWriter().write(objectMapper.writeValueAsString(error));


    }
}