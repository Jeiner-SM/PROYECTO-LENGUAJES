import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { environment } from '../../../../environment/environment';

export interface MensajeContactoRequest {
  nombre: string;
  email: string;
  asunto: string;
  mensaje: string;
}

@Injectable({
  providedIn: 'root',
})
export class ContactoService {

  private http = inject(HttpClient);
  private api = environment.apiUrl;

  enviarMensaje(data: MensajeContactoRequest) {
    return this.http.post(`${this.api}/contacto`, data);
  }
}
