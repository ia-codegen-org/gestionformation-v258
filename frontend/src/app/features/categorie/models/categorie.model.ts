// ─── Modèle généré automatiquement par IACodeGen

export interface Categorie {
  id?: number;
  nom: string;
  description: string;
  formations?: { id: number }[];
}

export interface CategorieCreateDTO {
  nom: string;
  description: string;
}

export interface CategorieUpdateDTO {
  nom?: string;
  description?: string;
}

export interface CategorieResponseDTO {
  id: number;
  nom: string;
  description: string;
  formationsIds?: number[];
}
