import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';

import { Router } from '@angular/router';
import { OfertaCard } from '../../../ofertas/ui/oferta-card/oferta-card';
import { OfertaFacade } from '../../../ofertas/data-access/oferta.facade';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    InputTextModule,
    OfertaCard
  ],
  templateUrl: './home.html',
  styleUrl: './home.scss', 
})
export class Home  {
  
  
  private router = inject(Router);
  private facade = inject(OfertaFacade);
  readonly ofertas = this.facade.ofertasDestacadas;

  
  searchPosition: string = '';
  searchLocation: string = '';
  loading: boolean = false;
  searchTags: string[] = ['Remoto', 'Desarrollo', 'Diseño', 'Marketing'];

  
  stats = {
    registeredCompanies: 150,
    totalJobs: 1200,
    activeApplicants: 5000
  };

 
  features = [
    { icon: 'pi pi-search', title: 'Búsqueda inteligente', description: 'Encuentra ofertas que se ajustan a tu perfil' },
    { icon: 'pi pi-bolt', title: 'Postulación rápida', description: 'Aplica a trabajos con un solo clic' },
    { icon: 'pi pi-chart-line', title: 'Seguimiento', description: 'Monitorea el estado de tus postulaciones' }
  ];

  



  searchJobs() {
    const queryParams: Record<string, string> = {};

    if (this.searchPosition.trim()) {
      queryParams['cargo'] = this.searchPosition.trim();
    }
    if (this.searchLocation.trim()) {
      queryParams['ubicacion'] = this.searchLocation.trim();
    }

    this.router.navigate(['/empleos'], { queryParams });
  }

  searchByTag(tag: string) {
    this.searchPosition = tag;
    this.searchJobs();
  }

  navigateToJobs() {
    this.router.navigate(['/empleos']); 
  }

  registerNow() {
    this.router.navigate(['/auth/register']);
  }
}