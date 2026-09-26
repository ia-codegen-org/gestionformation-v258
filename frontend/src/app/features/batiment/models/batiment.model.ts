// ─── Modèle généré automatiquement par IACodeGen

export interface Batiment {
  id?: number;
  nom: string;
  adresse: string;
  salles?: { id: number }[];
}

export interface BatimentCreateDTO {
  nom: string;
  adresse: string;
}

export interface BatimentUpdateDTO {
  nom?: string;
  adresse?: string;
}

export interface BatimentResponseDTO {
  id: number;
  nom: string;
  adresse: string;
  sallesIds?: number[];
}
