export type Modalidad = 'PRESENCIAL' | 'REMOTO' | 'HIBRIDO';

export interface OfertaResponse {
    id: number;
    titulo: string;
    descripcion: string;
    ubicacion: string;
    salario: number;
    modalidad: Modalidad;
    estado: string;
    nombreEmpresa: string;
}

export interface OfertaRequest {
    titulo: string;
    descripcion: string;
    ubicacion: string;
    salario: number;
    modalidad: string;
}

// Update parcial: el backend acepta cualquier subconjunto de estos campos (PATCH).
export interface OfertaUpdateRequest {
    titulo?: string;
    descripcion?: string;
    ubicacion?: string;
    salario?: number;
    modalidad?: string;
    estado?: string;
}

// Payload exclusivo para el endpoint de cambio rápido de estado.
export interface OfertaEstadoUpdateRequest {
    estado: string;
}

export interface EmpresaOfertaStats {
    totalOfertas: number;
    activas: number;
    cerradas: number;
}
