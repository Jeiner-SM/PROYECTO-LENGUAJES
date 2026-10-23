import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../environment/environment';
import { CurriculumVitaeDetalle, PostulantePerfil, PostulantePerfilUpdate } from './postulante-perfil.model';

@Injectable({
  providedIn: 'root'
})
export class PostulantePerfilService {

  private http = inject(HttpClient);
  private api = environment.apiUrl;

  obtenerMiPerfil() {
    return this.http.get<PostulantePerfil>(`${this.api}/postulantes/mi-perfil`);
  }

  actualizarMiPerfil(data: PostulantePerfilUpdate) {
    return this.http.put<PostulantePerfil>(`${this.api}/postulantes/mi-perfil`, data);
  }

  // Usado por la empresa para ver el CV estructurado de un candidato (botón "Ver CV").
  obtenerCvPorId(curriculumId: number) {
    return this.http.get<CurriculumVitaeDetalle>(`${this.api}/cv/${curriculumId}`);
  }
}
