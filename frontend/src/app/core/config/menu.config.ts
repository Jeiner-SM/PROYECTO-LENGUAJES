import { MenuItem } from "./menu.item";


export const MENU_ITEMS: MenuItem[] = [

  
  {
    label: 'Panel de Control',
    icon: 'pi pi-home',
    route: '/dashboard',
    roles: ['ROLE_ADMIN']
  },
  {
    label: 'Usuarios',
    icon: 'pi pi-users',
    route: '/admin/usuarios',
    roles: ['ROLE_ADMIN']
  },
  {
    label: 'Empresas',
    icon: 'pi pi-building',
    route: '/admin/empresas',
    roles: ['ROLE_ADMIN']
  },
  {
    label: 'Ofertas',
    icon: 'pi pi-briefcase',
    route: '/dashboard/jobs',
    roles: ['ROLE_ADMIN']
  },
  {
    label: 'Postulaciones',
    icon: 'pi pi-file',
    route: '/admin/postulaciones',
    roles: ['ROLE_ADMIN']
  },

  
  {
    label: 'Mi Panel',
    icon: 'pi pi-home',
    route: '',
    roles: ['ROLE_EMPRESA']
  },
  {
    label: 'Mis Ofertas',
    icon: 'pi pi-briefcase',
    route: 'jobs',
    roles: ['ROLE_EMPRESA']
  },
  {
    label: 'Crear Oferta',
    icon: 'pi pi-plus',
    route: 'jobs/create',
    roles: ['ROLE_EMPRESA']
  },
  {
    label: 'Postulantes',
    icon: 'pi pi-users',
    route: '/empresa/postulantes',
    roles: ['ROLE_EMPRESA']
  },
  {
    label: 'Mi Perfil',
    icon: 'pi pi-user',
    route: '/empresa/perfil',
    roles: ['ROLE_EMPRESA']
  },

  
  {
    label: 'Mi Panel',
    icon: 'pi pi-home',
    route: '/postulante/dashboard',
    roles: ['ROLE_POSTULANTE']
  },
  {
    label: 'Buscar Empleos',
    icon: 'pi pi-search',
    route: '/postulante/ofertas',
    roles: ['ROLE_POSTULANTE']
  },
  {
    label: 'Mis Postulaciones',
    icon: 'pi pi-file',
    route: '/postulante/postulaciones',
    roles: ['ROLE_POSTULANTE']
  },
  {
    label: 'Mi CV',
    icon: 'pi pi-id-card',
    route: '/postulante/cv',
    roles: ['ROLE_POSTULANTE']
  },
  {
    label: 'Mi Perfil',
    icon: 'pi pi-user',
    route: '/postulante/perfil',
    roles: ['ROLE_POSTULANTE']
  }
];