import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { TagModule } from 'primeng/tag';
import { SkeletonModule } from 'primeng/skeleton';

import { AdminService } from '../../data-access/admin.service';
import { MensajeContacto, UsuarioAdmin } from '../../data-access/admin.model';

// Resumen general del panel admin: conteos rápidos de usuarios por rol,
// cuentas deshabilitadas y los últimos mensajes de contacto recibidos.
@Component({
  selector: 'app-panel-control',
  standalone: true,
  imports: [CommonModule, TagModule, SkeletonModule],
  templateUrl: './panel-control.html',
  styleUrl: './panel-control.scss'
})
export class PanelControl implements OnInit {

  private adminService = inject(AdminService);
  private router = inject(Router);

  usuarios = signal<UsuarioAdmin[]>([]);
  mensajes = signal<MensajeContacto[]>([]);
  loading = signal(true);
  // Loading independiente para las 4 tarjetas de métricas (dependen de "usuarios"),
  // así no muestran "0" mientras el request todavía está en vuelo.
  loadingMetrics = signal(true);

  totalPostulantes = computed(() => this.usuarios().filter(u => u.rol === 'POSTULANTE').length);
  totalEmpresas = computed(() => this.usuarios().filter(u => u.rol === 'EMPRESA').length);
  totalDeshabilitados = computed(() => this.usuarios().filter(u => !u.activo).length);
  totalMensajes = computed(() => this.mensajes().length);

  ultimosMensajes = computed(() => this.mensajes().slice(0, 5));

  ngOnInit(): void {
    this.cargarDatos();
  }

  cargarDatos(): void {
    this.loading.set(true);
    this.loadingMetrics.set(true);

    this.adminService.listarUsuarios({ page: 0, size: 500 }).subscribe({
      next: (res) => {
        this.usuarios.set(res.content);
        this.loadingMetrics.set(false);
      },
      error: () => {
        this.usuarios.set([]);
        this.loadingMetrics.set(false);
      }
    });

    this.adminService.listarMensajes(0, 500).subscribe({
      next: (res) => {
        this.mensajes.set(res.content);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }

  irAUsuarios(): void {
    this.router.navigate(['/dashboard/admin/usuarios']);
  }

  irAMensajes(): void {
    this.router.navigate(['/dashboard/admin/mensajes']);
  }
}
