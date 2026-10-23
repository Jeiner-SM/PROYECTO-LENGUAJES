import { inject } from '@angular/core';
import {
  patchState,
  signalStore,
  withMethods,
  withState
} from '@ngrx/signals';

import { EmpresaState } from './empresa.state';
import { EmpresaService } from './empresa.service';

export const EmpresaStore = signalStore(
  { providedIn: 'root' },

  withState<EmpresaState>({
    empresas: [],
    loading: false
  }),

  withMethods((store, service = inject(EmpresaService)) => ({

    loadEmpresas() {

      patchState(store, {
        loading: true
      });

      service.listar().subscribe({
        next: (empresas) => {

          patchState(store, {
            empresas,
            loading: false
          });

        },

        error: (error) => {

          console.error(error);

          patchState(store, {
            loading: false
          });

        }
      });

    }

  }))
);