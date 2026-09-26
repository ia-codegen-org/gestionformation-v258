// ─── Modèle généré automatiquement par IACodeGen

export interface Etudiant {
  id?: number;
  username: string;
  email: string;
  role: string;
  nom: string;
  prenom: string;
  dateNaissance: string;
  sessions?: { id: number }[];
  evaluations?: { id: number }[];
  certificats?: { id: number }[];
  inscriptions?: { id: number }[];
  noteMoyenne?: number;
  dureeTotaleFormation?: number;
  nbCertificats?: number;
}

export interface EtudiantCreateDTO {
  username: string;
  email: string;
  password: string;
  nom: string;
  prenom: string;
  dateNaissance: string;
}

export interface EtudiantUpdateDTO {
  username?: string;
  email?: string;
  nom?: string;
  prenom?: string;
  dateNaissance?: string;
}

export interface EtudiantResponseDTO {
  id: number;
  username: string;
  email: string;
  role: string;
  nom: string;
  prenom: string;
  dateNaissance: string;
  sessionsIds?: number[];
  evaluationsIds?: number[];
  certificatsIds?: number[];
  inscriptionsIds?: number[];
  noteMoyenne?: number;
  dureeTotaleFormation?: number;
  nbCertificats?: number;
}
