// ─── Modèle généré automatiquement par IACodeGen

export interface Salle {
  id?: number;
  numero: string;
  capaciteMax: number;
  equipement: string;
  batiment?: { id: number };
  sessions?: { id: number }[];
}

export interface SalleCreateDTO {
  numero: string;
  capaciteMax: number;
  equipement: string;
  batimentId: number;
}

export interface SalleUpdateDTO {
  numero?: string;
  capaciteMax?: number;
  equipement?: string;
  batimentId?: number;
}

export interface SalleResponseDTO {
  id: number;
  numero: string;
  capaciteMax: number;
  equipement: string;
  batimentId?: number;
  sessionsIds?: number[];
}
