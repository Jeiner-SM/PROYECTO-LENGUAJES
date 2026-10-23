import { Component, inject, OnInit, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { TooltipModule } from 'primeng/tooltip';
import { SkeletonModule } from 'primeng/skeleton';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { ToastModule } from 'primeng/toast';

import { OfertaFacade } from '../../../data-access/oferta.facade';
import { OfertaService } from '../../../data-access/oferta.service';
import { OfertaResponse } from '../../../data-access/oferta.model';
import { MessageServices } from '../../../../../core/services/messages/message-service';
import { signal } from '@angular/core';

@Component({
  selector: 'app-list',
  standalone: true,
  imports: [
    CommonModule,
    TableModule,
    ButtonModule,
    InputTextModule,
    TooltipModule,
    SkeletonModule,
    IconFieldModule,
    InputIconModule,
    ToastModule,
  ],
  templateUrl: './list.html',
  styleUrl: './list.scss',
})
export class List implements OnInit {

  private facade = inject(OfertaFacade);
  private ofertaService = inject(OfertaService);
  private messageService = inject(MessageServices);
  private router = inject(Router);

  readonly ofertas = this.facade.ofertas;
  readonly loading = this.facade.loading;

  searchValue = signal('');
  eliminandoId: number | null = null;
  cambiandoEstadoId: number | null = null;

  estadosDisponibles = [
    { label: 'Activa', value: 'ACTIVA' },
    { label: 'Pausada', value: 'PAUSADA' },
    { label: 'Cerrada', value: 'CERRADA' },
  ];

  ofertasFiltradas = computed(() => {
    const q = this.searchValue().toLowerCase();
    if (!q) return this.ofertas();
    return this.ofertas().filter(o =>
      o.titulo?.toLowerCase().includes(q) ||
      o.nombreEmpresa?.toLowerCase().includes(q) ||
      o.ubicacion?.toLowerCase().includes(q)
    );
  });

  // El estado real es un string ("ACTIVA"/"CERRADA"/...), no un booleano.
  totalActivas = computed(() => this.ofertas().filter(o => o.estado === 'ACTIVA').length);
  totalCerradas = computed(() => this.ofertas().filter(o => o.estado === 'CERRADA').length);

  ngOnInit(): void {
    this.facade.cargarEmpresa();
  }

  onSearch(event: Event) {
    this.searchValue.set((event.target as HTMLInputElement).value);
  }

  irACrear(): void {
    this.router.navigate(['/dashboard/jobs/create']);
  }

  editar(ofertaId: number): void {
    this.router.navigate(['/dashboard/jobs/edit', ofertaId]);
  }

  verPostulantes(ofertaId: number): void {
    this.router.navigate(['/dashboard/jobs', ofertaId, 'postulantes']);
  }

  eliminar(ofertaId: number): void {
    const confirmado = confirm('¿Seguro que deseas eliminar esta oferta? Esta acción no se puede deshacer.');
    if (!confirmado) return;

    this.eliminandoId = ofertaId;
    this.ofertaService.eliminar(ofertaId).subscribe({
      next: () => {
        this.eliminandoId = null;
        this.messageService.showSuccess('La oferta se eliminó correctamente.');
        this.facade.cargarEmpresa();
      },
      error: (err) => {
        this.eliminandoId = null;
        const mensaje = err.error?.message || 'No se pudo eliminar la oferta.';
        this.messageService.showError(mensaje);
      }
    });
  }

  getEstadoLabel(estado: string): string {
    switch (estado) {
      case 'ACTIVA': return 'Activa';
      case 'CERRADA': return 'Cerrada';
      case 'PAUSADA': return 'Pausada';
      case 'BORRADOR': return 'Borrador';
      default: return estado;
    }
  }

  getModalidadLabel(modalidad: string): string {
    switch (modalidad) {
      case 'PRESENCIAL': return 'Presencial';
      case 'REMOTO': return 'Remoto';
      case 'HIBRIDO': return 'Híbrido';
      default: return modalidad;
    }
  }

  // Cambia el estado de la oferta desde la tabla (sin recargar la página):
  // llama al endpoint dedicado y actualiza solo esa fila en el store.
  cambiarEstado(oferta: OfertaResponse, event: Event): void {
    const nuevoEstado = (event.target as HTMLSelectElement).value;
    if (nuevoEstado === oferta.estado) return;

    this.cambiandoEstadoId = oferta.id;
    this.ofertaService.cambiarEstado(oferta.id, nuevoEstado).subscribe({
      next: (actualizada) => {
        this.cambiandoEstadoId = null;
        this.facade.actualizarOfertaLocal(oferta.id, { estado: actualizada.estado });
        this.messageService.showSuccess('El estado de la oferta se actualizó correctamente.');
      },
      error: (err) => {
        this.cambiandoEstadoId = null;
        (event.target as HTMLSelectElement).value = oferta.estado;
        const mensaje = err.error?.message || 'No se pudo actualizar el estado de la oferta.';
        this.messageService.showError(mensaje);
      }
    });
  }
}
