// ─── Modèle généré automatiquement par IACodeGen

export interface Formation {
  id?: number;
  titre: string;
  description: string;
  dureeHeures: number;
  categorie?: { id: number };
  formateurs?: { id: number }[];
  sessions?: { id: number }[];
  tauxReussite?: number;
}

export interface FormationCreateDTO {
  titre: string;
  description: string;
  dureeHeures: number;
  categorieId: number;
}

export interface FormationUpdateDTO {
  titre?: string;
  description?: string;
  dureeHeures?: number;
  categorieId?: number;
}

export interface FormationResponseDTO {
  id: number;
  titre: string;
  description: string;
  dureeHeures: number;
  categorieId?: number;
  formateursIds?: number[];
  sessionsIds?: number[];
  tauxReussite?: number;
}
