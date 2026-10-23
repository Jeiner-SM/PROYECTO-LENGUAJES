
export interface OfertaLaboral {
  id: number;
  titulo: string;
  descripcion: string;
  empresa: {
    id: number;
    nombre: string;
  };
  ubicacion: string;
  salario: number;
  tipoContrato: string;
  fechaPublicacion: string;
}