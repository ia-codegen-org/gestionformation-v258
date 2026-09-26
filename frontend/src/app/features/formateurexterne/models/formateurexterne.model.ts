// ─── Modèle généré automatiquement par IACodeGen

export interface FormateurExterne {
  id?: number;
  username: string;
  email: string;
  role: string;
  nom: string;
  prenom: string;
  anneesExperience: number;
  tarifJournalier: number;
}

export interface FormateurExterneCreateDTO {
  username: string;
  email: string;
  password: string;
  nom: string;
  prenom: string;
  anneesExperience: number;
  tarifJournalier: number;
}

export interface FormateurExterneUpdateDTO {
  username?: string;
  email?: string;
  nom?: string;
  prenom?: string;
  anneesExperience?: number;
  tarifJournalier?: number;
}

export interface FormateurExterneResponseDTO {
  id: number;
  username: string;
  email: string;
  role: string;
  nom: string;
  prenom: string;
  anneesExperience: number;
  tarifJournalier: number;
}
