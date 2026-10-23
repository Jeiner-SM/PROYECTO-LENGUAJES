import { UsuarioRegisterRequest } from './EmpresaRegisterRequest';

// Debe coincidir exactamente con PostulanteRequest del backend.
export interface PostulanteRegisterRequest {
  usuario: UsuarioRegisterRequest;
  direccion?: string;
  genero?: string;
  fechaNacimiento?: string;
}
