import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { EmpresaOfertaStats, OfertaEstadoUpdateRequest, OfertaRequest, OfertaResponse, OfertaUpdateRequest } from "./oferta.model";
import { environment } from '../../../environment/environment';
import { PageResponse } from "../../../shared/models/page.response";

@Injectable({
   providedIn: 'root'
})
export class OfertaService {

    private http = inject(HttpClient);
    private api = environment.apiUrl;

    listarPublicas(page = 0, size = 10) {
        return this.http.get<PageResponse<OfertaResponse>>(
            `${this.api}/ofertas/public`,
            { params: { page: page.toString(), size: size.toString() } }
        );
    }

    listarEmpresa(page = 0, size = 50) {
        return this.http.get<PageResponse<OfertaResponse>>(
            `${this.api}/empresa/ofertas`,
            { params: { page: page.toString(), size: size.toString() } }
        );
    }

    listarAdmin() {
        return this.http.get<PageResponse<OfertaResponse>>(`${this.api}/admin/ofertas`);
    }

    obtenerEstadisticasEmpresa() {
        return this.http.get<EmpresaOfertaStats>(`${this.api}/empresa/ofertas/stats`);
    }

    obtenerPorIdEmpresa(id: number) {
        return this.http.get<OfertaResponse>(`${this.api}/empresa/ofertas/${id}`);
    }

    crear(request: OfertaRequest) {
        return this.http.post<OfertaResponse>(`${this.api}/empresa/ofertas`, request);
    }

    eliminar(id: number) {
        return this.http.delete<void>(`${this.api}/empresa/ofertas/${id}`);
    }

    verDetalle(id: number) {
        return this.http.get<OfertaResponse>(`${this.api}/ofertas/public/${id}`);
    }

    actualizar(id: number, request: OfertaUpdateRequest) {
        return this.http.patch<OfertaResponse>(`${this.api}/empresa/ofertas/${id}`, request);
    }

    cambiarEstado(id: number, estado: string) {
        const request: OfertaEstadoUpdateRequest = { estado };
        return this.http.patch<OfertaResponse>(`${this.api}/empresa/ofertas/${id}/estado`, request);
    }
}
