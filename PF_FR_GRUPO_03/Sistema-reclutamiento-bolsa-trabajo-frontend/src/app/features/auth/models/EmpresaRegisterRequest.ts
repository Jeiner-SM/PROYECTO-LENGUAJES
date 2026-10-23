export interface UsuarioRegisterRequest {
  nombre: string;
  apellido: string;
  email: string;
  password: string;
}

// Debe coincidir exactamente con EmpresaRequestDTO del backend.
export interface EmpresaRegisterRequest {
  nombreEmpresa: string;
  ruc: string;
  razonSocial: string;
  descripcion?: string;
  direccion?: string;
  paginaWeb?: string;
  usuario: UsuarioRegisterRequest;
}
