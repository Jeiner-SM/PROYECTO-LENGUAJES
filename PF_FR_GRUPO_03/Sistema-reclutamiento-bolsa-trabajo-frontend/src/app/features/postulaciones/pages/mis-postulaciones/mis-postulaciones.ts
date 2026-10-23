import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { ButtonModule } from 'primeng/button';
import { SkeletonModule } from 'primeng/skeleton';
import { ToastModule } from 'primeng/toast';

import { PostulacionService } from '../../data-access/postulacion.service';
import { PostulacionResponse } from '../../data-access/postulacion.model';
import { MessageServices } from '../../../../core/services/messages/message-service';

@Component({
  selector: 'app-mis-postulaciones',
  standalone: true,
  imports: [CommonModule, RouterLink, TableModule, TagModule, ButtonModule, SkeletonModule, ToastModule],
  templateUrl: './mis-postulaciones.html',
  styleUrl: './mis-postulaciones.scss'
})
export class MisPostulaciones implements OnInit {

  private postulacionService = inject(PostulacionService);
  private messageService = inject(MessageServices);

  postulaciones = signal<PostulacionResponse[]>([]);
  loading = signal(true);
  cancelandoId = signal<number | null>(null);

  private readonly ESTADOS_NO_CANCELABLES = ['CANCELADO', 'RECHAZADO', 'ACEPTADO'];

  ngOnInit(): void {
    this.cargarPostulaciones();
  }

  cargarPostulaciones(): void {
    this.loading.set(true);

    this.postulacionService.listarMisPostulaciones().subscribe({
      next: (postulaciones) => {
        this.postulaciones.set(postulaciones);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.messageService.showError('No se pudieron cargar tus postulaciones.');
      }
    });
  }

  puedeCancelar(postulacion: PostulacionResponse): boolean {
    return !this.ESTADOS_NO_CANCELABLES.includes(postulacion.estado);
  }

  cancelar(postulacion: PostulacionResponse): void {
    if (this.cancelandoId() || !this.puedeCancelar(postulacion)) {
      return;
    }

    const confirmado = window.confirm(
      `¿Seguro que deseas anular tu postulación a "${postulacion.ofertaTitulo}"?`
    );

    if (!confirmado) {
      return;
    }

    this.cancelandoId.set(postulacion.id);

    this.postulacionService.cancelar(postulacion.id).subscribe({
      next: (actualizada) => {
        this.cancelandoId.set(null);
        this.postulaciones.update(lista =>
          lista.map(p => (p.id === postulacion.id ? actualizada : p))
        );
        this.messageService.showSuccess('Postulación anulada correctamente.');
      },
      error: (err) => {
        this.cancelandoId.set(null);
        const mensaje = err.error?.message || 'No se pudo anular la postulación.';
        this.messageService.showError(mensaje);
      }
    });
  }

  getSeverity(estado: string): 'success' | 'danger' | 'info' | 'warn' | 'secondary' {
    switch (estado) {
      case 'ACEPTADO':
        return 'success';
      case 'RECHAZADO':
      case 'CANCELADO':
        return 'danger';
      case 'ENTREVISTA':
        return 'info';
      case 'EN_REVISION':
        return 'warn';
      default:
        return 'secondary';
    }
  }
}
