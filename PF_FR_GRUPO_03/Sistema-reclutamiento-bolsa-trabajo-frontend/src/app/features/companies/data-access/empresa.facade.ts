import { inject, Injectable } from '@angular/core';
import { EmpresaStore } from './empresa.store';

@Injectable({
  providedIn: 'root'
})
export class EmpresaFacade {

  private store = inject(EmpresaStore);

  readonly empresas = this.store.empresas;
  readonly loading = this.store.loading;

  cargarEmpresas() {
    this.store.loadEmpresas();
  }

}