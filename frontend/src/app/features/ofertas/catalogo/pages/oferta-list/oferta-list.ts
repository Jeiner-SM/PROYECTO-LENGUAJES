import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { OfertaStore } from '../../../data-access/oferta.store';
import { OfertaCard } from '../../../ui/oferta-card/oferta-card';

@Component({
  selector: 'app-oferta-list',
  standalone: true,
  imports: [CommonModule, RouterLink, FormsModule, OfertaCard],
  templateUrl: './oferta-list.html',
  styleUrl: './oferta-list.scss'
})
export class OfertaList implements OnInit {
  // Inyectamos nuestro poderoso Store
  private ofertaStore = inject(OfertaStore);
  private route = inject(ActivatedRoute);

  // Mapeamos los Signals EXACTAMENTE como tu HTML los pide
  ofertas = this.ofertaStore.ofertas;
  loading = this.ofertaStore.loading;

  // Fase 2: buscador básico por título de la oferta o nombre de la empresa
  criterioBusqueda = signal('');

  // Filtro adicional de ubicación, recibido desde el buscador rápido del Home (?ubicacion=)
  criterioUbicacion = signal('');

  ofertasFiltradas = computed(() => {
    const criterio = this.criterioBusqueda().trim().toLowerCase();
    const ubicacion = this.criterioUbicacion().trim().toLowerCase();

    return this.ofertas().filter(oferta => {
      const coincideCriterio = !criterio ||
        oferta.titulo?.toLowerCase().includes(criterio) ||
        oferta.nombreEmpresa?.toLowerCase().includes(criterio);

      const coincideUbicacion = !ubicacion ||
        oferta.ubicacion?.toLowerCase().includes(ubicacion);

      return coincideCriterio && coincideUbicacion;
    });
  });

  ngOnInit(): void {
    // Disparamos la carga de las ofertas públicas desde el backend
    this.ofertaStore.loadPublicas();

    // El buscador rápido del Home redirige aquí con ?cargo=...&ubicacion=...
    const params = this.route.snapshot.queryParamMap;
    const cargo = params.get('cargo');
    const ubicacion = params.get('ubicacion');

    if (cargo) {
      this.criterioBusqueda.set(cargo);
    }
    if (ubicacion) {
      this.criterioUbicacion.set(ubicacion);
    }
  }

  filtrarEmpleos(valor: string): void {
    this.criterioBusqueda.set(valor);
  }
}