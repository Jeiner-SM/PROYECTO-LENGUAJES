import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

import { ButtonModule } from 'primeng/button';
import { TableModule } from 'primeng/table';
import { TagModule } from 'primeng/tag';
import { InputTextModule } from 'primeng/inputtext';
import { TooltipModule } from 'primeng/tooltip';
import { SkeletonModule } from 'primeng/skeleton';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';

import { OfertaFacade } from '../../../ofertas/data-access/oferta.facade';
import { OfertaService } from '../../../ofertas/data-access/oferta.service';
import { EmpresaOfertaStats } from '../../../ofertas/data-access/oferta.model';

/**
 * Panel de Control de la empresa: resumen de sus ofertas publicadas
 * (tarjetas de conteo + buscador + tabla), tal como en la referencia de diseño.
 */
@Component({
  selector: 'app-company-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    ButtonModule,
    TableModule,
    TagModule,
    InputTextModule,
    TooltipModule,
    SkeletonModule,
    IconFieldModule,
    InputIconModule,
  ],
  templateUrl: './company-dashboard.html',
  styleUrl: './company-dashboard.scss'
})
export class CompanyDashboard implements OnInit {

  private facade = inject(OfertaFacade);
  private ofertaService = inject(OfertaService);
  private router = inject(Router);

  readonly ofertas = this.facade.ofertas;
  readonly loading = this.facade.loading;

  stats = signal<EmpresaOfertaStats>({ totalOfertas: 0, activas: 0, cerradas: 0 });
  loadingStats = signal(true);

  searchValue = signal('');

  ofertasFiltradas = computed(() => {
    const q = this.searchValue().toLowerCase();
    if (!q) return this.ofertas();
    return this.ofertas().filter(o =>
      o.titulo?.toLowerCase().includes(q) ||
      o.nombreEmpresa?.toLowerCase().includes(q) ||
      o.ubicacion?.toLowerCase().includes(q)
    );
  });

  ngOnInit(): void {
    this.facade.cargarEmpresa();
    this.cargarEstadisticas();
  }

  cargarEstadisticas(): void {
    this.loadingStats.set(true);
    this.ofertaService.obtenerEstadisticasEmpresa().subscribe({
      next: (stats) => {
        this.stats.set(stats);
        this.loadingStats.set(false);
      },
      error: () => this.loadingStats.set(false)
    });
  }

  onSearch(event: Event) {
    this.searchValue.set((event.target as HTMLInputElement).value);
  }

  irACrear(): void {
    this.router.navigate(['/dashboard/jobs/create']);
  }

  verPostulantes(ofertaId: number): void {
    this.router.navigate(['/dashboard/jobs', ofertaId, 'postulantes']);
  }

  getSeverity(estado: string): 'success' | 'danger' | 'warn' | 'secondary' {
    switch (estado) {
      case 'ACTIVA': return 'success';
      case 'CERRADA': return 'danger';
      case 'PAUSADA': return 'warn';
      default: return 'secondary';
    }
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
}
