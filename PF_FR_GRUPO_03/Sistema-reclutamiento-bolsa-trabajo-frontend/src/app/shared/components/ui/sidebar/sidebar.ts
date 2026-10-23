import { Component, OnInit, inject, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { TokenService } from '../../../../core/services/token/token';
import { AdminService } from '../../../../features/admin/data-access/admin.service';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.scss' // Manteniendo tu diseño original intacto
})
export class Sidebar implements OnInit {
  private router = inject(Router);
  private tokenService = inject(TokenService);
  private adminService = inject(AdminService);

  // Variables reactivas (Signals) que tu HTML original espera
  menuItems = signal<any[]>([]);
  nombreUsuario = signal<string>('Usuario');
  rolUsuario = signal<string>('Desconocido');
  iniciales = signal<string>('U');

  // Contador de empresas pendientes de verificación, mostrado como badge junto
  // a "Gestión de Usuarios" (solo aplica para el rol ADMIN).
  empresasPendientes = signal<number>(0);

  ngOnInit(): void {
    // El rol y el nombre visible se leen directamente del JWT (decodificado en TokenService),
    // así siempre reflejan al usuario realmente autenticado, incluso tras recargar la página.
    const rolConPrefijo = this.tokenService.getRoles()[0] || 'ROLE_POSTULANTE';
    const rol = rolConPrefijo.replace('ROLE_', '');
    const nombre = this.tokenService.getNombre() || 'Usuario';

    // Seteamos las variables del UI
    this.rolUsuario.set(rol);
    this.nombreUsuario.set(nombre);
    this.iniciales.set(nombre.charAt(0).toUpperCase());

    // Asignamos el menú respetando tu estructura de rutas
  if (rol.includes('POSTULANTE')) {
  this.menuItems.set([
    { label: 'Buscar Empleos', icon: 'pi pi-briefcase', route: '/dashboard/ofertas' },
    { label: 'Mis Postulaciones', icon: 'pi pi-file-check', route: '/dashboard/postulaciones' },
    { label: 'Mi Perfil / CV', icon: 'pi pi-user-edit', route: '/dashboard/perfil' }
  ]);

  
  } else if (rol.includes('EMPRESA')) {
      this.menuItems.set([
        { label: 'Panel de Control', icon: 'pi pi-chart-bar', route: '/dashboard/empresa' },
        { label: 'Publicar Empleo', icon: 'pi pi-plus-circle', route: '/dashboard/jobs/create' },
        { label: 'Gestionar Ofertas', icon: 'pi pi-list', route: '/dashboard/jobs' },
        { label: 'Configuración de Cuenta', icon: 'pi pi-cog', route: '/dashboard/empresa/perfil' }
      ]);
    } else if (rol.includes('ADMIN')) {
      this.menuItems.set([
        { label: 'Panel de Control', icon: 'pi pi-sliders-h', route: '/dashboard/admin' },
        { label: 'Gestión de Usuarios', icon: 'pi pi-users', route: '/dashboard/admin/usuarios', badgeKey: 'empresasPendientes' },
        { label: 'Mensajes de Contacto', icon: 'pi pi-envelope', route: '/dashboard/admin/mensajes' }
      ]);
      this.cargarEmpresasPendientes();
    }
  }

  private cargarEmpresasPendientes(): void {
    this.adminService.contarEmpresasPendientes().subscribe({
      next: (count) => this.empresasPendientes.set(count),
      error: () => this.empresasPendientes.set(0)
    });
  }

  logout(): void {
    // Limpia tokens en localStorage y también el signal de permisos en memoria,
    // evitando que queden datos de la sesión anterior si otro usuario inicia sesión después.
    this.tokenService.clearTokens();
    this.router.navigate(['/auth/login']);
  }
}