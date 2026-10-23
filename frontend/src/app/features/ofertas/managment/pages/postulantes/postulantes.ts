import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { SelectModule } from 'primeng/select';
import { SkeletonModule } from 'primeng/skeleton';
import { ToastModule } from 'primeng/toast';
import { DialogModule } from 'primeng/dialog';

import { PostulacionService } from '../../../../postulaciones/data-access/postulacion.service';
import { PostulacionResponse } from '../../../../postulaciones/data-access/postulacion.model';
import { OfertaService } from '../../../data-access/oferta.service';
import { MessageServices } from '../../../../../core/services/messages/message-service';
import { PostulantePerfilService } from '../../../../postulantes/data-access/postulante-perfil.service';
import { CurriculumVitaeDetalle } from '../../../../postulantes/data-access/postulante-perfil.model';

@Component({
  selector: 'app-postulantes',
  standalone: true,
  imports: [
    CommonModule,
    DatePipe,
    FormsModule,
    ButtonModule,
    TableModule,
    TagModule,
    SelectModule,
    SkeletonModule,
    ToastModule,
    DialogModule,
  ],
  templateUrl: './postulantes.html',
  styleUrl: './postulantes.scss',
})
export class Postulantes implements OnInit {

  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private postulacionService = inject(PostulacionService);
  private ofertaService = inject(OfertaService);
  private postulantePerfilService = inject(PostulantePerfilService);
  private messageService = inject(MessageServices);

  ofertaId!: number;
  tituloOferta = '';
  loading = true;
  actualizandoId: number | null = null;

  postulaciones: PostulacionResponse[] = [];

  // Modal "Ver CV" (currículum estructurado del candidato)
  cvDialogVisible = false;
  cargandoCv = false;
  cvSeleccionado: CurriculumVitaeDetalle | null = null;

  estadosDisponibles = [
    { label: 'Enviado', value: 'ENVIADO' },
    { label: 'En revisión', value: 'EN_REVISION' },
    { label: 'Entrevista', value: 'ENTREVISTA' },
    { label: 'Aceptado', value: 'ACEPTADO' },
    { label: 'Rechazado', value: 'RECHAZADO' },
  ];

  ngOnInit(): void {
    this.ofertaId = Number(this.route.snapshot.paramMap.get('id'));
    this.cargarOferta();
    this.cargarPostulantes();
  }

  cargarOferta(): void {
    this.ofertaService.obtenerPorIdEmpresa(this.ofertaId).subscribe({
      next: (oferta) => this.tituloOferta = oferta.titulo,
      error: () => {}
    });
  }

  cargarPostulantes(): void {
    this.loading = true;
    this.postulacionService.listarPorOferta(this.ofertaId).subscribe({
      next: (postulaciones) => {
        this.postulaciones = postulaciones;
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        const mensaje = err.error?.message || 'No se pudieron cargar los postulantes.';
        this.messageService.showError(mensaje);
      }
    });
  }

  cambiarEstado(postulacion: PostulacionResponse, nuevoEstado: string): void {
    if (nuevoEstado === postulacion.estado) return;

    this.actualizandoId = postulacion.id;
    this.postulacionService.actualizarEstado(postulacion.id, nuevoEstado).subscribe({
      next: (actualizada) => {
        this.actualizandoId = null;
        postulacion.estado = actualizada.estado;
        this.messageService.showSuccess('El estado de la postulación se actualizó.');
      },
      error: (err) => {
        this.actualizandoId = null;
        const mensaje = err.error?.message || 'No se pudo actualizar el estado.';
        this.messageService.showError(mensaje);
      }
    });
  }

  volver(): void {
    this.router.navigate(['/dashboard/jobs']);
  }

  verCv(postulacion: PostulacionResponse): void {
    if (!postulacion.curriculumId) return;

    this.cvDialogVisible = true;
    this.cargandoCv = true;
    this.cvSeleccionado = null;

    this.postulantePerfilService.obtenerCvPorId(postulacion.curriculumId).subscribe({
      next: (cv) => {
        this.cvSeleccionado = cv;
        this.cargandoCv = false;
      },
      error: () => {
        this.cargandoCv = false;
        this.cvDialogVisible = false;
        this.messageService.showError('No se pudo cargar el CV del candidato.');
      }
    });
  }

  getSeverity(estado: string): 'success' | 'danger' | 'warn' | 'info' | 'secondary' {
    switch (estado) {
      case 'ACEPTADO': return 'success';
      case 'RECHAZADO': return 'danger';
      case 'ENTREVISTA': return 'warn';
      case 'EN_REVISION': return 'info';
      case 'CANCELADO': return 'secondary';
      default: return 'secondary';
    }
  }

  getEstadoLabel(estado: string): string {
    const encontrado = this.estadosDisponibles.find(e => e.value === estado);
    return encontrado ? encontrado.label : (estado === 'CANCELADO' ? 'Cancelado' : estado);
  }
}
