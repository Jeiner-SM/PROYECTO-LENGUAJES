export interface PostulantePerfil {
  postulanteId: number;
  nombre: string;
  apellido: string;
  email: string;
  fechaNacimiento?: string | null;
  genero?: string | null;
  direccion?: string | null;
  tituloProfesional?: string | null;
  descripcion?: string | null;
  experiencia?: string | null;
  habilidades?: string | null;
  educacion?: string | null;
  telefono?: string | null;
  linkedin?: string | null;
}

// Currículum estructurado de un candidato, tal como lo expone GET /api/cv/{id}.
// Lo usa la empresa para "Ver CV" desde la tabla de postulantes.
export interface CurriculumVitaeDetalle {
  id: number;
  tituloProfesional?: string | null;
  descripcion?: string | null;
  experiencia?: string | null;
  habilidades?: string | null;
  educacion?: string | null;
  telefono?: string | null;
  linkedin?: string | null;
  nombreUsuario?: string | null;
}

export interface PostulantePerfilUpdate {
  nombre: string;
  apellido: string;
  fechaNacimiento?: string | null;
  genero?: string | null;
  direccion?: string | null;
  tituloProfesional?: string | null;
  descripcion?: string | null;
  experiencia?: string | null;
  habilidades?: string | null;
  educacion?: string | null;
  telefono?: string | null;
  linkedin?: string | null;
}
