// ─── Modèle généré automatiquement par IACodeGen

export interface Departement {
  id?: number;
  nom: string;
  budget: number;
  formateurs?: { id: number }[];
  chiffreAffairesTotal?: number;
}

export interface DepartementCreateDTO {
  nom: string;
  budget: number;
}

export interface DepartementUpdateDTO {
  nom?: string;
  budget?: number;
}

export interface DepartementResponseDTO {
  id: number;
  nom: string;
  budget: number;
  formateursIds?: number[];
  chiffreAffairesTotal?: number;
}
