import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { TokenService } from '../../../core/services/token/token';

/**
 * Componente "puente" para la ruta vacía de /dashboard.
 * Evita que el dashboard quede en blanco tras el login: redirige
 * automáticamente a la vista principal según el rol del usuario.
 */
@Component({
  selector: 'app-dashboard-home',
  standalone: true,
  template: `<div class="dashboard-home-loading">Cargando tu panel...</div>`,
  styles: [`
    .dashboard-home-loading {
      display: flex;
      align-items: center;
      justify-content: center;
      height: 60vh;
      color: #6b7280;
      font-size: 0.95rem;
    }
  `]
})
export class DashboardHome implements OnInit {
  private router = inject(Router);
  private tokenService = inject(TokenService);

  ngOnInit(): void {
    const roles = this.tokenService.getRoles();

    if (roles.includes('ROLE_EMPRESA')) {
      this.router.navigate(['/dashboard/empresa']);
    } else if (roles.includes('ROLE_ADMIN')) {
      this.router.navigate(['/dashboard/admin']);
    } else {
      // Por defecto (ROLE_POSTULANTE u otro rol), mostramos "Buscar Empleos"
      this.router.navigate(['/dashboard/ofertas']);
    }
  }
}
