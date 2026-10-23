import { OfertaResponse } from "./oferta.model";

export interface OfertaState {
  ofertas: OfertaResponse[];
  loading: boolean;
  selected?: OfertaResponse | null;
}