// ─── Modèle généré automatiquement par IACodeGen

export interface Session {
  id?: number;
  dateDebut: string;
  dateFin: string;
  formation?: { id: number };
  formateur?: { id: number };
  salle?: { id: number };
  etudiants?: { id: number }[];
  inscriptions?: { id: number }[];
  evaluations?: { id: number }[];
  nbEtudiants?: number;
  coutTotal?: number;
}

export interface SessionCreateDTO {
  dateDebut: string;
  dateFin: string;
  formationId: number;
  formateurId: number;
  salleId: number;
}

export interface SessionUpdateDTO {
  dateDebut?: string;
  dateFin?: string;
  formationId?: number;
  formateurId?: number;
  salleId?: number;
}

export interface SessionResponseDTO {
  id: number;
  dateDebut: string;
  dateFin: string;
  formationId?: number;
  formateurId?: number;
  salleId?: number;
  etudiantsIds?: number[];
  inscriptionsIds?: number[];
  evaluationsIds?: number[];
  nbEtudiants?: number;
  coutTotal?: number;
}
