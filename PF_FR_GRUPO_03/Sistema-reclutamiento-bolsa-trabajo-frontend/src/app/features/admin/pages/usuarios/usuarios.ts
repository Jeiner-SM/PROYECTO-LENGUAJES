import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { InputTextModule } from 'primeng/inputtext';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { SkeletonModule } from 'primeng/skeleton';
import { ToggleSwitchModule } from 'primeng/toggleswitch';
import { ToastModule } from 'primeng/toast';
import { TabsModule } from 'primeng/tabs';
import { SelectModule } from 'primeng/select';
import { ButtonModule } from 'primeng/button';

import { AdminService } from '../../data-access/admin.service';
import { EmpresaAdmin, EstadoValidacionEmpresa, UsuarioAdmin } from '../../data-access/admin.model';
import { MessageServices } from '../../../../core/services/messages/message-service';

type FiltroEstado = 'TODOS' | 'HABILITADAS' | 'DESHABILITADAS';
type FiltroValidacion = 'TODOS' | 'PENDIENTE' | 'ACTIVO' | 'RECHAZADO';

// "Gestión de Usuarios" del panel admin: dos pestañas (Postulantes / Empresas),
// cada una con su propio filtro de estado y, en el caso de empresas, filtro
// adicional por estado de validación + acción para verificar cuentas pendientes.
@Component({
  selector: 'app-usuarios',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    TagModule,
    InputTextModule,
    IconFieldModule,
    InputIconModule,
    SkeletonModule,
    ToggleSwitchModule,
    ToastModule,
    TabsModule,
    SelectModule,
    ButtonModule,
  ],
  templateUrl: './usuarios.html',
  styleUrl: './usuarios.scss'
})
export class Usuarios implements OnInit {

  private adminService = inject(AdminService);
  private messageService = inject(MessageServices);

  // ---- Pestaña Postulantes ----
  postulantes = signal<UsuarioAdmin[]>([]);
  loadingPostulantes = signal(true);
  cambiandoIdPostulante = signal<number | null>(null);
  searchPostulantes = signal('');
  filtroEstadoPostulantes = signal<FiltroEstado>('TODOS');

  opcionesEstado: { label: string; value: FiltroEstado }[] = [
    { label: 'Todas', value: 'TODOS' },
    { label: 'Habilitadas', value: 'HABILITADAS' },
    { label: 'Deshabilitadas', value: 'DESHABILITADAS' },
  ];

  postulantesFiltrados = computed(() => {
    const q = this.searchPostulantes().toLowerCase();
    return this.postulantes().filter(u =>
      !q ||
      u.nombre?.toLowerCase().includes(q) ||
      u.apellido?.toLowerCase().includes(q) ||
      u.email?.toLowerCase().includes(q)
    );
  });

  // ---- Pestaña Empresas ----
  empresas = signal<EmpresaAdmin[]>([]);
  loadingEmpresas = signal(true);
  cambiandoIdEmpresa = signal<number | null>(null);
  verificandoId = signal<number | null>(null);
  searchEmpresas = signal('');
  filtroEstadoEmpresas = signal<FiltroEstado>('TODOS');
  filtroValidacion = signal<FiltroValidacion>('TODOS');

  opcionesValidacion: { label: string; value: FiltroValidacion }[] = [
    { label: 'Todas', value: 'TODOS' },
    { label: 'Pendientes', value: 'PENDIENTE' },
    { label: 'Verificadas', value: 'ACTIVO' },
    { label: 'Rechazadas', value: 'RECHAZADO' },
  ];

  empresasFiltradas = computed(() => {
    const q = this.searchEmpresas().toLowerCase();
    return this.empresas().filter(e =>
      !q ||
      e.nombreEmpresa?.toLowerCase().includes(q) ||
      e.email?.toLowerCase().includes(q) ||
      e.ruc?.toLowerCase().includes(q)
    );
  });

  // Conteo global de empresas pendientes (independiente de los filtros de la tabla),
  // usado en el badge de la pestaña "Empresas".
  empresasPendientesCount = signal(0);
  loadingPendientesCount = signal(true);

  ngOnInit(): void {
    this.cargarPostulantes();
    this.cargarEmpresas();
    this.cargarConteoPendientes();
  }

  cargarConteoPendientes(): void {
    this.loadingPendientesCount.set(true);
    this.adminService.contarEmpresasPendientes().subscribe({
      next: (count) => {
        this.empresasPendientesCount.set(count);
        this.loadingPendientesCount.set(false);
      },
      error: () => {
        this.empresasPendientesCount.set(0);
        this.loadingPendientesCount.set(false);
      }
    });
  }

  // ---------------- Postulantes ----------------

  cargarPostulantes(): void {
    this.loadingPostulantes.set(true);
    const activo = this.mapFiltroEstado(this.filtroEstadoPostulantes());

    this.adminService.listarUsuarios({ rol: 'POSTULANTE', activo, page: 0, size: 500 }).subscribe({
      next: (res) => {
        this.postulantes.set(res.content);
        this.loadingPostulantes.set(false);
      },
      error: () => {
        this.loadingPostulantes.set(false);
        this.messageService.showError('No se pudo cargar la lista de postulantes.');
      }
    });
  }

  onFiltroEstadoPostulantesChange(): void {
    this.cargarPostulantes();
  }

  onSearchPostulantes(event: Event): void {
    this.searchPostulantes.set((event.target as HTMLInputElement).value);
  }

  onTogglePostulante(usuario: UsuarioAdmin, nuevoEstado: boolean): void {
    this.cambiandoIdPostulante.set(usuario.id);

    this.adminService.cambiarEstadoUsuario(usuario.id, nuevoEstado).subscribe({
      next: (actualizado) => {
        this.cambiandoIdPostulante.set(null);
        this.postulantes.update(lista =>
          lista.map(u => u.id === usuario.id ? { ...u, activo: actualizado.activo } : u)
        );
        this.messageService.showSuccess(
          actualizado.activo
            ? `La cuenta de ${usuario.nombre} fue habilitada correctamente.`
            : `La cuenta de ${usuario.nombre} fue deshabilitada correctamente.`
        );
      },
      error: (err) => {
        this.cambiandoIdPostulante.set(null);
        const mensaje = err.error?.message || 'No se pudo actualizar el estado de la cuenta.';
        this.messageService.showError(mensaje);
      }
    });
  }

  // ---------------- Empresas ----------------

  cargarEmpresas(): void {
    this.loadingEmpresas.set(true);
    const activo = this.mapFiltroEstado(this.filtroEstadoEmpresas());
    const filtroValidacionActual = this.filtroValidacion();
    const estadoValidacion = filtroValidacionActual === 'TODOS' ? null : filtroValidacionActual;

    this.adminService.listarEmpresas({ activo, estadoValidacion, page: 0, size: 500 }).subscribe({
      next: (res) => {
        this.empresas.set(res.content);
        this.loadingEmpresas.set(false);
      },
      error: () => {
        this.loadingEmpresas.set(false);
        this.messageService.showError('No se pudo cargar la lista de empresas.');
      }
    });
  }

  onFiltroEmpresasChange(): void {
    this.cargarEmpresas();
  }

  onSearchEmpresas(event: Event): void {
    this.searchEmpresas.set((event.target as HTMLInputElement).value);
  }

  onToggleEmpresa(empresa: EmpresaAdmin, nuevoEstado: boolean): void {
    this.cambiandoIdEmpresa.set(empresa.id);

    this.adminService.cambiarEstadoUsuario(empresa.usuarioId, nuevoEstado).subscribe({
      next: (actualizado) => {
        this.cambiandoIdEmpresa.set(null);
        this.empresas.update(lista =>
          lista.map(e => e.id === empresa.id ? { ...e, activo: actualizado.activo } : e)
        );
        this.messageService.showSuccess(
          actualizado.activo
            ? `La cuenta de ${empresa.nombreEmpresa} fue habilitada correctamente.`
            : `La cuenta de ${empresa.nombreEmpresa} fue deshabilitada correctamente.`
        );
      },
      error: (err) => {
        this.cambiandoIdEmpresa.set(null);
        const mensaje = err.error?.message || 'No se pudo actualizar el estado de la cuenta.';
        this.messageService.showError(mensaje);
      }
    });
  }

  verificarEmpresa(empresa: EmpresaAdmin): void {
    this.verificandoId.set(empresa.id);

    this.adminService.verificarEmpresa(empresa.id).subscribe({
      next: (actualizada) => {
        this.verificandoId.set(null);
        this.empresas.update(lista =>
          lista.map(e => e.id === empresa.id ? { ...e, estadoValidacion: actualizada.estadoValidacion } : e)
        );
        this.cargarConteoPendientes();
        this.messageService.showSuccess(`La empresa ${empresa.nombreEmpresa} fue verificada correctamente.`);
      },
      error: (err) => {
        this.verificandoId.set(null);
        const mensaje = err.error?.message || 'No se pudo verificar la empresa.';
        this.messageService.showError(mensaje);
      }
    });
  }

  // ---------------- Utilidades ----------------

  private mapFiltroEstado(filtro: FiltroEstado): boolean | null {
    if (filtro === 'HABILITADAS') return true;
    if (filtro === 'DESHABILITADAS') return false;
    return null;
  }

  getValidacionLabel(estado: EstadoValidacionEmpresa): string {
    switch (estado) {
      case 'ACTIVO': return 'Verificada';
      case 'RECHAZADO': return 'Rechazada';
      default: return 'Pendiente';
    }
  }

  getValidacionSeverity(estado: EstadoValidacionEmpresa): 'success' | 'warn' | 'danger' {
    switch (estado) {
      case 'ACTIVO': return 'success';
      case 'RECHAZADO': return 'danger';
      default: return 'warn';
    }
  }
}
