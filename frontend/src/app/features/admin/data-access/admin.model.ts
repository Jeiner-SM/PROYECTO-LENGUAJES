export interface UsuarioAdmin {
  id: number;
  nombre: string;
  apellido: string;
  email: string;
  rol: string;
  nombreEmpresa?: string | null;
  activo: boolean;
  fechaCreacion: string;
}

export type EstadoValidacionEmpresa = 'PENDIENTE' | 'ACTIVO' | 'RECHAZADO';

// Debe coincidir con EmpresaAdminResponseDTO del backend.
export interface EmpresaAdmin {
  id: number; // id de la Empresa (usado para verificar)
  usuarioId: number; // id del Usuario asociado (usado para habilitar/deshabilitar)
  nombreEmpresa: string;
  ruc: string;
  email: string;
  estadoValidacion: EstadoValidacionEmpresa;
  activo: boolean;
  fechaCreacion: string;
}

export interface MensajeContacto {
  id: number;
  nombre: string;
  email: string;
  asunto: string;
  mensaje: string;
  fechaEnvio: string;
}
