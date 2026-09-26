// ─── Modèle généré automatiquement par IACodeGen

export interface Formateur {
  id?: number;
  username: string;
  email: string;
  role: string;
  nom: string;
  prenom: string;
  anneesExperience: number;
  departement?: { id: number };
  formations?: { id: number }[];
  sessions?: { id: number }[];
  nbSessions?: number;
  chiffreAffairesTotal?: number;
}

export interface FormateurCreateDTO {
  username: string;
  email: string;
  password: string;
  nom: string;
  prenom: string;
  anneesExperience: number;
  departementId: number;
}

export interface FormateurUpdateDTO {
  username?: string;
  email?: string;
  nom?: string;
  prenom?: string;
  anneesExperience?: number;
  departementId?: number;
}

export interface FormateurResponseDTO {
  id: number;
  username: string;
  email: string;
  role: string;
  nom: string;
  prenom: string;
  anneesExperience: number;
  departementId?: number;
  formationsIds?: number[];
  sessionsIds?: number[];
  nbSessions?: number;
  chiffreAffairesTotal?: number;
}
