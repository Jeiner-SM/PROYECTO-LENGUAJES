import { Routes } from "@angular/router";

export const OFERTAS_MANAGMENT_ROUTE: Routes = [

    {
        path: '',
        loadComponent: () =>
            import('./pages/list/list').then((p) => p.List)
    },
    {
        path: 'create',
        loadComponent: () =>
            import('./pages/create/create').then((p) => p.Create)
    },
    {
        path: 'edit/:id',
        loadComponent: () =>
            import('./pages/edit/edit').then((p) => p.Edit)
    },
    {
        path: ':id/postulantes',
        loadComponent: () =>
            import('./pages/postulantes/postulantes').then((p) => p.Postulantes)
    }

]
