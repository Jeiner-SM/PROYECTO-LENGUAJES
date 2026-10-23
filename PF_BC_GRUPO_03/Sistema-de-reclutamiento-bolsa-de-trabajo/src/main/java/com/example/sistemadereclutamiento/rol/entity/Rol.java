package com.example.sistemadereclutamiento.rol.entity;

import com.example.sistemadereclutamiento.permiso.entity.Permiso;
import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;
@Entity
@Data
@Table(name = "roles")
@NoArgsConstructor
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @BatchSize(size = 30)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "rol_permisos",
            joinColumns = @JoinColumn(name = "rol_id"),
            inverseJoinColumns = @JoinColumn(name = "permiso_id")

    )
    private Set<Permiso> permisos= new HashSet<>();

}
