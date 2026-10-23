import { Component, inject, OnInit, signal } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { Button } from 'primeng/button';
import { ToastModule } from 'primeng/toast';
import { OfertaStore } from '../../../data-access/oferta.store';
import { OfertaDetail } from '../../../ui/oferta-detail/oferta-detail';
import { PostulacionService } from '../../../../postulaciones/data-access/postulacion.service';
import { MessageServices } from '../../../../../core/services/messages/message-service';

@Component({
  selector: 'app-oferta-detail-page',
  standalone: true,
  imports: [CommonModule, OfertaDetail, Button, ToastModule],
  templateUrl: './oferta-detail.html',
  styleUrl: './oferta-detail.scss'
})
export class OfertaDetailPage implements OnInit {

  private route = inject(ActivatedRoute);
  private ofertaStore = inject(OfertaStore);
  private postulacionService = inject(PostulacionService);
  private messageService = inject(MessageServices);

  oferta = this.ofertaStore.selected ?? signal<undefined>(undefined);
  loading = this.ofertaStore.loading;

  ofertaId: number | null = null;
  yaPostulo = false;
  postulando = false;

  // Fase 2: evita el "parpadeo" del botón Postular mientras se confirma
  // con el backend si el usuario ya se postuló a esta oferta.
  isLoadingPostulacion = true;

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');

    if (id) {
      this.ofertaId = Number(id);
      this.ofertaStore.loadById(this.ofertaId);
      this.verificarSiYaPostulo(this.ofertaId);
    } else {
      this.isLoadingPostulacion = false;
    }
  }

  private verificarSiYaPostulo(ofertaId: number): void {
    this.isLoadingPostulacion = true;

    this.postulacionService.listarMisPostulaciones().subscribe({
      next: (postulaciones) => {
        // Una postulación CANCELADA no cuenta como "ya postulado": el backend
        // permite volver a postularse a la misma oferta en ese caso.
        this.yaPostulo = postulaciones.some(p => p.ofertaId === ofertaId && p.estado !== 'CANCELADO');
        this.isLoadingPostulacion = false;
      },
      error: () => {
        // Si falla la verificación, el botón queda habilitado y el POST validará
        this.isLoadingPostulacion = false;
      }
    });
  }

  postular(): void {
    if (!this.ofertaId || this.yaPostulo || this.postulando) {
      return;
    }

    this.postulando = true;

    this.postulacionService.postular({ ofertaId: this.ofertaId }).subscribe({
      next: () => {
        this.postulando = false;
        this.yaPostulo = true;
        this.messageService.showSuccess('¡Postulación enviada con éxito!');
      },
      error: (err) => {
        this.postulando = false;
        const mensaje = err.error?.message || 'No se pudo completar la postulación.';
        this.messageService.showError(mensaje);
      }
    });
  }
}