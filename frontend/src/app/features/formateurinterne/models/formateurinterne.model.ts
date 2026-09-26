// ─── Modèle généré automatiquement par IACodeGen

export interface FormateurInterne {
  id?: number;
  username: string;
  email: string;
  role: string;
  nom: string;
  prenom: string;
  anneesExperience: number;
  salaireMensuel: number;
}

export interface FormateurInterneCreateDTO {
  username: string;
  email: string;
  password: string;
  nom: string;
  prenom: string;
  anneesExperience: number;
  salaireMensuel: number;
}

export interface FormateurInterneUpdateDTO {
  username?: string;
  email?: string;
  nom?: string;
  prenom?: string;
  anneesExperience?: number;
  salaireMensuel?: number;
}

export interface FormateurInterneResponseDTO {
  id: number;
  username: string;
  email: string;
  role: string;
  nom: string;
  prenom: string;
  anneesExperience: number;
  salaireMensuel: number;
}
