import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environment/environment';
import { PageResponse } from '../../../shared/models/page.response';
import { EmpresaAdmin, EstadoValidacionEmpresa, MensajeContacto, UsuarioAdmin } from './admin.model';

export interface ListarUsuariosOptions {
  rol?: string;
  activo?: boolean | null;
  page?: number;
  size?: number;
}

export interface ListarEmpresasOptions {
  estadoValidacion?: EstadoValidacionEmpresa | null;
  activo?: boolean | null;
  page?: number;
  size?: number;
}

@Injectable({
  providedIn: 'root',
})
export class AdminService {

  private http = inject(HttpClient);
  private api = environment.apiUrl;

  listarUsuarios(opciones: ListarUsuariosOptions = {}) {
    const { rol, activo, page = 0, size = 50 } = opciones;
    const params: Record<string, string> = { page: page.toString(), size: size.toString() };
    if (rol) params['rol'] = rol;
    if (activo !== undefined && activo !== null) params['activo'] = activo.toString();

    return this.http.get<PageResponse<UsuarioAdmin>>(`${this.api}/admin/usuarios`, { params });
  }

  cambiarEstadoUsuario(id: number, activo: boolean) {
    return this.http.patch<UsuarioAdmin>(`${this.api}/admin/usuarios/${id}/estado`, { activo });
  }

  listarEmpresas(opciones: ListarEmpresasOptions = {}) {
    const { estadoValidacion, activo, page = 0, size = 50 } = opciones;
    const params: Record<string, string> = { page: page.toString(), size: size.toString() };
    if (estadoValidacion) params['estadoValidacion'] = estadoValidacion;
    if (activo !== undefined && activo !== null) params['activo'] = activo.toString();

    return this.http.get<PageResponse<EmpresaAdmin>>(`${this.api}/admin/empresas`, { params });
  }

  contarEmpresasPendientes() {
    return this.http.get<number>(`${this.api}/admin/empresas/pendientes/count`);
  }

  verificarEmpresa(id: number) {
    return this.http.patch<EmpresaAdmin>(`${this.api}/admin/empresas/${id}/verificar`, {});
  }

  listarMensajes(page = 0, size = 50) {
    return this.http.get<PageResponse<MensajeContacto>>(`${this.api}/admin/mensajes-contacto`, {
      params: { page: page.toString(), size: size.toString() }
    });
  }
}
