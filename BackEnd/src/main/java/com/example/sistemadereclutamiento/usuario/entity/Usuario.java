package com.example.sistemadereclutamiento.usuario.entity;

import com.example.sistemadereclutamiento.rol.entity.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String nombre;

    private String apellido;

    private String email;

    private String password;

    private boolean activo = true;

    @Column(name = "fecha_creacion", insertable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    // roles es EAGER (se necesita en cada autenticación), pero al listar páginas de
    // usuarios (ej. panel admin) Hibernate haría 1 SELECT extra por usuario para
    // inicializar esta colección. @BatchSize agrupa esos SELECTs en lotes de 30
    // (WHERE usuario_id IN (?, ?, ...)) en vez de 1 query por fila, eliminando el N+1.
    @BatchSize(size = 30)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles = new HashSet<>();
}