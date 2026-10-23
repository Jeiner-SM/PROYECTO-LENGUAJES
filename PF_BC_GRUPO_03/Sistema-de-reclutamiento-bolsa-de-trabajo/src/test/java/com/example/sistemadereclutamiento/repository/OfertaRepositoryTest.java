package com.example.sistemadereclutamiento.repository;


import com.example.sistemadereclutamiento.empresa.entity.Empresa;
import com.example.sistemadereclutamiento.empresa.repository.EmpresaRepository;
import com.example.sistemadereclutamiento.oferta.entity.Oferta;
import com.example.sistemadereclutamiento.oferta.entity.OfertaEstado;
import com.example.sistemadereclutamiento.oferta.repository.OfertaRepository;
import com.example.sistemadereclutamiento.usuario.entity.Usuario;
import com.example.sistemadereclutamiento.usuario.repository.UsuarioRepositorio;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)

@DataJpaTest

class OfertaRepositoryTest {

    @Autowired
    private OfertaRepository ofertaRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Test
    void testGuardarOferta() {

        Empresa empresa = new Empresa();
        empresa.setNombreEmpresa("Empresa Test");


        Usuario usuario = new Usuario();
        usuario.setNombre("Test");
        usuario.setApellido("Test");
        usuario.setEmail("test@test.com");
        usuario.setPassword("1234");

        empresa.setUsuario(usuario);

        usuarioRepositorio.save(usuario);
        empresa = empresaRepository.save(empresa);

        Oferta oferta = new Oferta();
        oferta.setTitulo("Programador");
        oferta.setDescripcion("Trabajo remoto");
        oferta.setUbicacion("Lima");
        oferta.setSalario(2500.0);
        oferta.setEstado(OfertaEstado.ACTIVA);
        oferta.setEmpresa(empresa);

        Oferta guardada = ofertaRepository.save(oferta);

        assertNotNull(guardada.getId());
        assertEquals("Programador", guardada.getTitulo());
    }
}