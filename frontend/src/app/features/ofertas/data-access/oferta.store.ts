import { computed, inject } from "@angular/core"; // <-- IMPORTANTE: Agregamos computed
import { OfertaService } from "./oferta.service";
import { OfertaResponse } from "./oferta.model";
import { OfertaState } from "./oferta.state";
import { patchState, signalStore, withComputed, withMethods, withState } from '@ngrx/signals';

export const OfertaStore = signalStore(
    { providedIn: 'root' },

    withState<OfertaState>({
        ofertas: [],
        loading: false,
        selected: null
    }),

    withMethods((store, service = inject(OfertaService)) => ({

        loadPublicas() {
            patchState(store, { loading: true });

            service.listarPublicas().subscribe((page: any) => {
                patchState(store, {
                    ofertas: page.content || page, // Lo blindamos por si acaso no viene page.content
                    loading: false
                });
            });
        },

        loadEmpresa() {
            patchState(store, { loading: true });

            service.listarEmpresa().subscribe((page: any) => {
                console.log('Respuesta empresa:', page);
                patchState(store, {
                    ofertas: page.content || page,
                    loading: false
                });
            });
        },

        loadAdmin() {
            patchState(store, { loading: true });

            service.listarAdmin().subscribe((page: any) => {
                patchState(store, {
                    ofertas: page.content || page,
                    loading: false
                });
            });
        },
        
        loadById(id: number) {
            patchState(store, { loading: true });

            service.verDetalle(id).subscribe(oferta => {
                patchState(store, {
                    selected: oferta,
                    loading: false
                });
            });
        },

        // Actualiza una oferta en memoria (ej. tras cambiar su estado) sin
        // tener que recargar toda la lista desde el backend.
        actualizarOfertaLocal(id: number, cambios: Partial<OfertaResponse>) {
            patchState(store, {
                ofertas: store.ofertas().map(o => o.id === id ? { ...o, ...cambios } : o)
            });
        }
    })),
    
    // CORRECCIÓN DEL ERROR AQUÍ:
    withComputed((store) => ({
        ofertasDestacadas: computed(() => store.ofertas().slice(0, 4))
    }))
);