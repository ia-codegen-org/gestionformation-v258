// ─── Modèle généré automatiquement par IACodeGen

export interface Certificat {
  id?: number;
  titre: string;
  dateObtention: string;
  mention: string;
  etudiant?: { id: number };
}

export interface CertificatCreateDTO {
  titre: string;
  dateObtention: string;
  mention: string;
  etudiantId: number;
}

export interface CertificatUpdateDTO {
  titre?: string;
  dateObtention?: string;
  mention?: string;
  etudiantId?: number;
}

export interface CertificatResponseDTO {
  id: number;
  titre: string;
  dateObtention: string;
  mention: string;
  etudiantId?: number;
}
