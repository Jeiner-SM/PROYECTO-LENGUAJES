import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth-guard';
import { noAuthGuard } from './core/guards/no-auth-guard';

export const routes: Routes = [
  // Rutas públicas (home, empleos, empresas, contacto): acceso libre, sin token ni redirección a login.
  {
    path: '',
    loadChildren: () => import('./layout/public-layout/public-layout.route').then(p => p.PUBLIC_LAYOUT_ROUTE)
  },
  {
    path: 'auth/login',
    canActivate: [noAuthGuard],
    loadComponent: () => import('./features/auth/pages/login/login').then(p => p.Login)
  },
  {
    path: 'auth/register',
    canActivate: [noAuthGuard],
    loadComponent: () => import('./features/auth/pages/register/register').then(p => p.Register)
  },
  {
    path: 'auth/register/postulante',
    canActivate: [noAuthGuard],
    loadComponent: () => import('./features/auth/pages/register/postulante-register/postulante-register').then(p => p.PostulanteRegister)
  },
  {
    path: 'auth/register/empresa',
    canActivate: [noAuthGuard],
    loadComponent: () => import('./features/auth/pages/register/empresa-register/empresa-register').then(p => p.EmpresaRegister)
  },
  // Página histórica nunca conectada al backend: se reemplazó por /dashboard/perfil (Fase 4).
  { path: 'auth/postulante/edit', redirectTo: 'dashboard/perfil' },
  {
    path: 'dashboard',
    canActivate: [authGuard], // El rolGuard se maneja mejor en las rutas hijas o en el layout
    loadChildren: () => import('./layout/dashboard-layout/dashboard-layout.route').then(p => p.DASHBOARD_LAYOUT_ROUTE)
  },
  // Ruta comodín: cualquier URL inválida muestra la página 404 en vez de redirigir silenciosamente.
  {
    path: '**',
    loadComponent: () => import('./shared/pages/not-found/not-found').then(c => c.NotFound)
  }
];