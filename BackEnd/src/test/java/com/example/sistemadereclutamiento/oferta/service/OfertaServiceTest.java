package com.example.sistemadereclutamiento.oferta.service;

import com.example.sistemadereclutamiento.empresa.entity.Empresa;
import com.example.sistemadereclutamiento.empresa.repository.EmpresaRepository;
import com.example.sistemadereclutamiento.oferta.dto.request.OfertaRequestDTO;
import com.example.sistemadereclutamiento.oferta.dto.response.OfertaResponseDTO;
import com.example.sistemadereclutamiento.oferta.entity.Oferta;
import com.example.sistemadereclutamiento.oferta.entity.OfertaEstado;
import com.example.sistemadereclutamiento.oferta.mapper.OfertaMapper;
import com.example.sistemadereclutamiento.oferta.repository.OfertaRepository;
import com.example.sistemadereclutamiento.shared.exeption.BusinessException;
import com.example.sistemadereclutamiento.shared.exeption.ResourceNotFoundException;
import com.example.sistemadereclutamiento.usuario.entity.Usuario;
import com.example.sistemadereclutamiento.usuario.service.UsuarioSecurity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias de {@link OfertaService} usando JUnit 5 + Mockito.
 * No se levanta el contexto de Spring: todas las dependencias (repositorios,
 * mapper y seguridad) se simulan con @Mock, y el servicio bajo prueba se
 * construye con @InjectMocks, tal como recomienda la rúbrica.
 */
@ExtendWith(MockitoExtension.class)
class OfertaServiceTest {

    @Mock
    private OfertaRepository ofertaRepository;

    @Mock
    private EmpresaRepository empresaRepository;

    @Mock
    private OfertaMapper ofertaMapper;

    @Mock
    private UsuarioSecurity usuarioSecurity;

    @InjectMocks
    private OfertaService ofertaService;

    // -----------------------------------------------------------------
    // guardarOferta()
    // -----------------------------------------------------------------

    @Test
    @DisplayName("guardarOferta: debe crear la oferta exitosamente cuando el usuario tiene empresa y rol EMPRESA")
    void testCrearOfertaExitoso() {
        Usuario usuarioLogueado = new Usuario();
        usuarioLogueado.setId(1L);

        Empresa empresa = new Empresa();
        empresa.setId(10L);
        empresa.setUsuario(usuarioLogueado);

        OfertaRequestDTO request = new OfertaRequestDTO();
        request.setTitulo("Desarrollador Backend");
        request.setDescripcion("Java y Spring Boot");
        request.setUbicacion("Lima");
        request.setSalario(3500.0);

        Oferta ofertaSinGuardar = new Oferta();
        ofertaSinGuardar.setTitulo("Desarrollador Backend");

        Oferta ofertaGuardada = new Oferta();
        ofertaGuardada.setId(100L);
        ofertaGuardada.setTitulo("Desarrollador Backend");
        ofertaGuardada.setEstado(OfertaEstado.ACTIVA);
        ofertaGuardada.setEmpresa(empresa);

        OfertaResponseDTO response = new OfertaResponseDTO();
        response.setId(100L);
        response.setTitulo("Desarrollador Backend");

        when(usuarioSecurity.usuarioLogado()).thenReturn(usuarioLogueado);
        when(empresaRepository.findEmpresasByUsuario_Id(1L)).thenReturn(Optional.of(empresa));
        when(usuarioSecurity.isEmpresa()).thenReturn(true);
        when(ofertaMapper.toEntity(request)).thenReturn(ofertaSinGuardar);
        when(ofertaRepository.save(any(Oferta.class))).thenReturn(ofertaGuardada);
        when(ofertaMapper.toDTO(ofertaGuardada)).thenReturn(response);

        OfertaResponseDTO resultado = ofertaService.guardarOferta(request);

        assertNotNull(resultado);
        assertEquals(100L, resultado.getId());
        assertEquals("Desarrollador Backend", resultado.getTitulo());
        verify(ofertaRepository, times(1)).save(any(Oferta.class));
    }

    @Test
    @DisplayName("guardarOferta: debe lanzar ResourceNotFoundException si el usuario no tiene empresa registrada")
    void testCrearOfertaLanzaExcepcionCuandoNoTieneEmpresa() {
        Usuario usuarioLogueado = new Usuario();
        usuarioLogueado.setId(1L);

        when(usuarioSecurity.usuarioLogado()).thenReturn(usuarioLogueado);
        when(empresaRepository.findEmpresasByUsuario_Id(1L)).thenReturn(Optional.empty());

        OfertaRequestDTO request = new OfertaRequestDTO();
        request.setTitulo("Desarrollador Backend");

        assertThrows(ResourceNotFoundException.class, () -> ofertaService.guardarOferta(request));
        verify(ofertaRepository, never()).save(any(Oferta.class));
    }

    @Test
    @DisplayName("guardarOferta: debe lanzar BusinessException si el usuario tiene empresa pero no rol EMPRESA")
    void testCrearOfertaLanzaExcepcionCuandoNoEsEmpresa() {
        Usuario usuarioLogueado = new Usuario();
        usuarioLogueado.setId(1L);

        Empresa empresa = new Empresa();
        empresa.setId(10L);

        when(usuarioSecurity.usuarioLogado()).thenReturn(usuarioLogueado);
        when(empresaRepository.findEmpresasByUsuario_Id(1L)).thenReturn(Optional.of(empresa));
        when(usuarioSecurity.isEmpresa()).thenReturn(false);

        OfertaRequestDTO request = new OfertaRequestDTO();

        assertThrows(BusinessException.class, () -> ofertaService.guardarOferta(request));
        verify(ofertaRepository, never()).save(any(Oferta.class));
    }

    // -----------------------------------------------------------------
    // obtenerPorId()
    // -----------------------------------------------------------------

    @Test
    @DisplayName("obtenerPorId: debe retornar la oferta cuando el ID existe")
    void testObtenerPorIdExitoso() {
        Oferta oferta = new Oferta();
        oferta.setId(5L);
        oferta.setTitulo("Analista QA");

        OfertaResponseDTO response = new OfertaResponseDTO();
        response.setId(5L);
        response.setTitulo("Analista QA");

        when(ofertaRepository.findById(5L)).thenReturn(Optional.of(oferta));
        when(ofertaMapper.toDTO(oferta)).thenReturn(response);

        OfertaResponseDTO resultado = ofertaService.obtenerPorId(5L);

        assertNotNull(resultado);
        assertEquals(5L, resultado.getId());
        assertEquals("Analista QA", resultado.getTitulo());
    }

    @Test
    @DisplayName("obtenerPorId: debe lanzar ResourceNotFoundException cuando el ID no existe")
    void testObtenerPorIdLanzaExcepcionCuandoNoExiste() {
        when(ofertaRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException excepcion = assertThrows(
                ResourceNotFoundException.class,
                () -> ofertaService.obtenerPorId(999L)
        );

        assertTrue(excepcion.getMessage().contains("999"));
        verify(ofertaMapper, never()).toDTO(any(Oferta.class));
    }

    // -----------------------------------------------------------------
    // eliminarOferta()
    // -----------------------------------------------------------------

    @Test
    @DisplayName("eliminarOferta: debe eliminar la oferta cuando el usuario logueado es el propietario")
    void testEliminarOfertaExitosoCuandoEsPropietario() {
        Usuario propietario = new Usuario();
        propietario.setId(1L);

        Empresa empresa = new Empresa();
        empresa.setId(10L);
        empresa.setUsuario(propietario);

        Oferta oferta = new Oferta();
        oferta.setId(7L);
        oferta.setEmpresa(empresa);

        when(ofertaRepository.findById(7L)).thenReturn(Optional.of(oferta));
        when(usuarioSecurity.isAdmin()).thenReturn(false);
        when(usuarioSecurity.usuarioLogado()).thenReturn(propietario);

        ofertaService.eliminarOferta(7L);

        verify(ofertaRepository, times(1)).delete(oferta);
    }

    @Test
    @DisplayName("eliminarOferta: debe lanzar AccessDeniedException si el usuario logueado no es el propietario")
    void testEliminarOfertaLanzaExcepcionCuandoNoEsPropietario() {
        Usuario propietario = new Usuario();
        propietario.setId(1L);

        Usuario otroUsuario = new Usuario();
        otroUsuario.setId(2L);

        Empresa empresa = new Empresa();
        empresa.setId(10L);
        empresa.setUsuario(propietario);

        Oferta oferta = new Oferta();
        oferta.setId(7L);
        oferta.setEmpresa(empresa);

        when(ofertaRepository.findById(7L)).thenReturn(Optional.of(oferta));
        when(usuarioSecurity.isAdmin()).thenReturn(false);
        when(usuarioSecurity.usuarioLogado()).thenReturn(otroUsuario);

        assertThrows(AccessDeniedException.class, () -> ofertaService.eliminarOferta(7L));
        verify(ofertaRepository, never()).delete(any(Oferta.class));
    }
}
