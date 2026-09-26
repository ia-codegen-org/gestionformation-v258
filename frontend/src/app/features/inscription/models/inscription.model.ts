// ─── Modèle généré automatiquement par IACodeGen

export interface Inscription {
  id?: number;
  statut: string;
  dateInscription: string;
  etudiant?: { id: number };
  session?: { id: number };
  paiement?: { id: number };
}

export interface InscriptionCreateDTO {
  statut: string;
  dateInscription: string;
  etudiantId: number;
  sessionId: number;
  paiementId: number;
}

export interface InscriptionUpdateDTO {
  statut?: string;
  dateInscription?: string;
  etudiantId?: number;
  sessionId?: number;
  paiementId?: number;
}

export interface InscriptionResponseDTO {
  id: number;
  statut: string;
  dateInscription: string;
  etudiantId?: number;
  sessionId?: number;
  paiementId?: number;
}
