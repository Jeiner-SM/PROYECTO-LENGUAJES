import { inject, Injectable } from "@angular/core";
import { OfertaStore } from "./oferta.store";
import { OfertaResponse } from "./oferta.model";
import { TokenService } from "../../../core/services/token/token";

@Injectable({
  providedIn: 'root'
})
export class OfertaFacade {

  private store = inject(OfertaStore);
  private authFacade = inject(TokenService);

  readonly ofertas = this.store.ofertas;
  readonly loading = this.store.loading;
readonly ofertaSeleccionada =
  this.store.selected;
  readonly ofertasDestacadas = this.store.ofertasDestacadas;

  cargarPublicas() {
    this.store.loadPublicas();
  }

  cargarEmpresa() {
    this.store.loadEmpresa();
  }

  cargarAdmin() {
    this.store.loadAdmin();
  }

  cargarPorId(id: number) {
    this.store.loadById(id);
  }

  actualizarOfertaLocal(id: number, cambios: Partial<OfertaResponse>) {
    this.store.actualizarOfertaLocal(id, cambios);
  }

  cargarOfertas() {

    const roles = this.authFacade.getRoles();

    console.log('Roles:', roles);

    if (roles.includes('ROLE_ADMIN')) {
      console.log('ADMIN');
      this.cargarAdmin();
      return;
    }

    if (roles.includes('ROLE_EMPRESA')) {
      console.log('EMPRESA');
      this.cargarEmpresa();
      return;
    }

    console.log('PUBLICAS');
    this.cargarPublicas();
  }
}