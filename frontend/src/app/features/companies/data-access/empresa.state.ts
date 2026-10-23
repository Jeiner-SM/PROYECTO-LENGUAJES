import { EmpresaResponse } from './empresa.model';

export interface EmpresaState {
  empresas: EmpresaResponse[];
  loading: boolean;
}