export interface PostulacionRequest {
  postulanteId: number;
  ofertaId: number;
  cvUrl?: string;
}

export interface PostulacionResponse {
  id: number;
  postulanteId: number;
  nombrePostulante?: string;
  emailPostulante?: string;
  ofertaId: number;
  ofertaTitulo: string;
  nombreEmpresa?: string;
  cvUrl?: string;
  curriculumId?: number;
  estado: string;
  fechaPostulacion: string;
}

export interface PostulanteMeResponse {
  postulanteId: number;
  usuarioId: number;
  email: string;
}

export interface PostularRequest {
  ofertaId: number;
  cvUrl?: string;
}