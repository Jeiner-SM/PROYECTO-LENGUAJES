import { Routes } from "@angular/router";

export const ADMIN_ROUTE: Routes = [
    {
        path: '',
        loadComponent: () =>
            import('./pages/panel-control/panel-control').then((p) => p.PanelControl)
    },
    {
        path: 'usuarios',
        loadComponent: () =>
            import('./pages/usuarios/usuarios').then((p) => p.Usuarios)
    },
    {
        path: 'mensajes',
        loadComponent: () =>
            import('./pages/mensajes/mensajes').then((p) => p.Mensajes)
    }
]
