import { Component, Input } from '@angular/core';
import { OfertaResponse } from '../../data-access/oferta.model';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-oferta-card',
  imports: [RouterLink],
  templateUrl: './oferta-card.html',
  styleUrl: './oferta-card.scss',
})
export class OfertaCard {

  @Input() oferta!: OfertaResponse;

  get ofertaempresa(): string[] {
    return this.oferta.nombreEmpresa.trim().split(/\s+/);
  }

  get resultado(): string {
    return this.ofertaempresa[0][0] + this.ofertaempresa[this.ofertaempresa.length - 1][0];
  }

  get isActiva(): boolean {
    return this.oferta.estado === 'ACTIVA';
  }

  get estadoLabel(): string {
    switch (this.oferta.estado) {
      case 'ACTIVA': return 'Activa';
      case 'PAUSADA': return 'Pausada';
      case 'CERRADA': return 'Cerrada';
      case 'BORRADOR': return 'Borrador';
      default: return this.oferta.estado;
    }
  }

  get modalidadLabel(): string {
    switch (this.oferta.modalidad) {
      case 'PRESENCIAL': return 'Presencial';
      case 'REMOTO': return 'Remoto';
      case 'HIBRIDO': return 'Híbrido';
      default: return this.oferta.modalidad || '';
    }
  }
}