import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { EmpresaPerfil, EmpresaPerfilUpdate, EmpresaResponse } from './empresa.model';
import { Observable } from 'rxjs';
import { environment } from '../../../environment/environment';

@Injectable({
  providedIn: 'root'
})
export class EmpresaService {

  private http = inject(HttpClient);

  private apiUrl = environment.apiUrl + '/empresas';

  listar(): Observable<EmpresaResponse[]> {
    return this.http.get<EmpresaResponse[]>(this.apiUrl);
  }

  obtenerMiPerfil(): Observable<EmpresaPerfil> {
    return this.http.get<EmpresaPerfil>(`${this.apiUrl}/mi-perfil`);
  }

  actualizarMiPerfil(data: EmpresaPerfilUpdate): Observable<EmpresaPerfil> {
    return this.http.put<EmpresaPerfil>(`${this.apiUrl}/mi-perfil`, data);
  }

}
