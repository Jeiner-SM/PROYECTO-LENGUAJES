export interface EmpresaResponse {
  id: number;
  nombreEmpresa: string;
  razonSocial: string;
  ruc: string;
  estadoValidacion: string;
  usuarioEmail: string;
}

export interface EmpresaRequest {

    nombre: string;
    ruc: string;
    correoCorporativo: string;
    telefono: string;
    direccion: string;
    descripcion: string;
    
}

// Debe coincidir con EmpresaPerfilResponseDTO del backend.
export interface EmpresaPerfil {
  id: number;
  nombreEmpresa: string;
  razonSocial: string;
  ruc: string;
  descripcion?: string;
  direccion?: string;
  paginaWeb?: string;
  estadoValidacion: string;
  email: string;
}

// Debe coincidir con EmpresaPerfilRequestDTO del backend (sin RUC ni estado: no editables desde el perfil).
export interface EmpresaPerfilUpdate {
  nombreEmpresa: string;
  razonSocial: string;
  descripcion?: string;
  direccion?: string;
  paginaWeb?: string;
}
