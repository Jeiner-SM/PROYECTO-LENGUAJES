import { Component, Input } from '@angular/core';
import { OfertaResponse } from '../../data-access/oferta.model';

@Component({
  selector: 'app-oferta-detail',
  imports: [],
  templateUrl: './oferta-detail.html',
  styleUrl: './oferta-detail.scss',
})
export class OfertaDetail {

  @Input({ required: true })
  oferta!: OfertaResponse;

  get resultado(): string {
    const palabras = this.oferta.nombreEmpresa.trim().split(/\s+/);
    return (
      palabras[0][0] +
      palabras[palabras.length - 1][0]
    ).toUpperCase();
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
}