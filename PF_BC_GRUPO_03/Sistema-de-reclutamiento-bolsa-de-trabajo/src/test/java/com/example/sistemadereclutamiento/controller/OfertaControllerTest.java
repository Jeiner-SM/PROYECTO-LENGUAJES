package com.example.sistemadereclutamiento.controller;

import com.example.sistemadereclutamiento.oferta.controller.OfertaAdminController;

import com.example.sistemadereclutamiento.oferta.service.OfertaService;
import com.example.sistemadereclutamiento.security.jtw.JwtService;
import com.example.sistemadereclutamiento.usuario.repository.UsuarioRepositorio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

// addFilters = false: este test cubre la lógica del controlador, no la
// seguridad (ya probada aparte); sin esto, JwtAuthenticationFilter +
// SecurityConfig exigirían un token y la petición devolvería 401.
@WebMvcTest(OfertaAdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class OfertaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfertaService ofertaService;

    // JwtAuthenticationFilter es un @Component (bean tipo Filter), así que el
    // slice de @WebMvcTest lo instancia igual aunque no se esté probando
    // seguridad aquí. Se mockean sus dependencias para que el contexto cargue.
    @MockBean
    private JwtService jwtService;

    @MockBean
    private UsuarioRepositorio usuarioRepositorio;

    // El endpoint tiene @PreAuthorize("hasAuthority('OFERTA_VIEW_ALL')"); ese
    // chequeo es un aspecto de Spring Security independiente de los filtros
    // (por eso sigue activo aunque addFilters = false), así que se necesita
    // un usuario simulado con esa authority para no recibir 403/401.
    @Test
    @WithMockUser(authorities = "OFERTA_VIEW_ALL")
    void testListarOfertas() throws Exception {

        when(ofertaService.obtenerOfertasAdmin(org.mockito.Mockito.any()))
                .thenReturn(new PageImpl<>(Collections.emptyList()));

        mockMvc.perform(get("/api/admin/ofertas"))
                .andExpect(status().isOk());
    }
}