import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, switchMap } from 'rxjs';
import { environment } from '../../../environment/environment';
import { PostulacionRequest, PostulacionResponse, PostulanteMeResponse, PostularRequest} from './postulacion.model';
@Injectable({
  providedIn: 'root'
})
export class PostulacionService {

  private http = inject(HttpClient);
  private api = environment.apiUrl;

  obtenerMiPostulanteId() {
    return this.http
      .get<PostulanteMeResponse>(`${this.api}/postulantes/me`)
      .pipe(map(response => response.postulanteId));
  }

  postular(request: PostularRequest) {
    return this.obtenerMiPostulanteId().pipe(
      switchMap(postulanteId => {
        const body: PostulacionRequest = {
          postulanteId,
          ofertaId: request.ofertaId,
          cvUrl: request.cvUrl
        };
        return this.http.post<PostulacionResponse>(`${this.api}/postulaciones`, body);
      })
    );
  }

  listarMisPostulaciones() {
    // El backend resuelve al postulante autenticado a partir del token,
    // evitando exponer/adivinar IDs de otros postulantes.
    return this.http.get<PostulacionResponse[]>(`${this.api}/postulaciones/mis-postulaciones`);
  }

  cancelar(postulacionId: number) {
    return this.http.patch<PostulacionResponse>(
      `${this.api}/postulaciones/${postulacionId}/cancelar`,
      {}
    );
  }

  // --- Uso desde la empresa ---

  listarPorOferta(ofertaId: number) {
    return this.http.get<PostulacionResponse[]>(`${this.api}/postulaciones/oferta/${ofertaId}`);
  }

  actualizarEstado(postulacionId: number, estado: string) {
    return this.http.patch<PostulacionResponse>(
      `${this.api}/postulaciones/${postulacionId}`,
      { estado }
    );
  }
}