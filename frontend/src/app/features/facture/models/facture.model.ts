// ─── Modèle généré automatiquement par IACodeGen

export interface Facture {
  id?: number;
  numero: string;
  date: string;
  montantTotal: number;
  statut: string;
  paiement?: { id: number };
}

export interface FactureCreateDTO {
  numero: string;
  date: string;
  montantTotal: number;
  statut: string;
  paiementId: number;
}

export interface FactureUpdateDTO {
  numero?: string;
  date?: string;
  montantTotal?: number;
  statut?: string;
  paiementId?: number;
}

export interface FactureResponseDTO {
  id: number;
  numero: string;
  date: string;
  montantTotal: number;
  statut: string;
  paiementId?: number;
}
