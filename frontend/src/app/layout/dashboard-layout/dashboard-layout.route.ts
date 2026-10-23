import { Routes } from '@angular/router';
import { DashboardLayout } from './dashboard-layout';
import { rolGuardGuard } from '../../core/guards/rol-guard-guard';

export const DASHBOARD_LAYOUT_ROUTE: Routes = [
  {
    path: '',
    component: DashboardLayout,
    children: [
      // Ruta vacía: evita que /dashboard quede en blanco. Redirige según el rol.
      {
        path: '',
        pathMatch: 'full',
        loadComponent: () => import('./dashboard-home/dashboard-home').then(c => c.DashboardHome)
      },
      // RUTA PARA POSTULANTE: Catálogo de empleos (Real)
      {
        path: 'ofertas', 
        loadComponent: () => import('../../features/ofertas/catalogo/pages/oferta-list/oferta-list').then(c => c.OfertaList),
        canActivate: [rolGuardGuard], 
        data: { roles: ['ROLE_POSTULANTE'] }
      },
      // RUTA PARA EL DETALLE (Para que puedan ver la info real de la oferta)
      {
        path: 'ofertas/:id',
        loadComponent: () => import('../../features/ofertas/catalogo/pages/oferta-detail/oferta-detail').then(c => c.OfertaDetailPage)
      },
      // RUTA PARA POSTULANTE: Mis Postulaciones (Fase 3)
      {
        path: 'postulaciones',
        loadComponent: () => import('../../features/postulaciones/pages/mis-postulaciones/mis-postulaciones').then(c => c.MisPostulaciones),
        canActivate: [rolGuardGuard],
        data: { roles: ['ROLE_POSTULANTE'] }
      },
      // RUTA PARA POSTULANTE: Mi Perfil / CV (Fase 4)
      {
        path: 'perfil',
        loadComponent: () => import('../../features/postulantes/pages/perfil/perfil-postulante').then(c => c.PerfilPostulante),
        canActivate: [rolGuardGuard],
        data: { roles: ['ROLE_POSTULANTE'] }
      },
      // RUTA PARA EMPRESA: Publicar/Gestionar ofertas y ver postulantes
      {
        path: 'jobs',
        loadChildren: () => import('../../features/ofertas/managment/Oferta.managment.route').then(c => c.OFERTAS_MANAGMENT_ROUTE),
        canActivate: [rolGuardGuard],
        data: { roles: ['ROLE_EMPRESA'] }
      },
      // RUTA PARA EMPRESA: Panel de Control (estadísticas + listado de ofertas)
      {
        path: 'empresa',
        loadComponent: () => import('../../features/companies/pages/company-dashboard/company-dashboard').then(c => c.CompanyDashboard),
        canActivate: [rolGuardGuard],
        data: { roles: ['ROLE_EMPRESA'] }
      },
      // RUTA PARA EMPRESA: Configuración de Cuenta (perfil de la empresa)
      {
        path: 'empresa/perfil',
        loadComponent: () => import('../../features/companies/pages/perfil-empresa/perfil-empresa').then(c => c.PerfilEmpresa),
        canActivate: [rolGuardGuard],
        data: { roles: ['ROLE_EMPRESA'] }
      },
      // RUTAS PARA ADMIN: Panel de Control, Gestión de Usuarios y Mensajes de Contacto
      {
        path: 'admin',
        loadChildren: () => import('../../features/admin/admin.route').then(c => c.ADMIN_ROUTE),
        canActivate: [rolGuardGuard],
        data: { roles: ['ROLE_ADMIN'] }
      }
    ]
  }
];