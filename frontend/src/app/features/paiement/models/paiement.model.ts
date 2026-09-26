// ─── Modèle généré automatiquement par IACodeGen

export interface Paiement {
  id?: number;
  montant: number;
  date: string;
  modePaiement: string;
  inscription?: { id: number };
  facture?: { id: number };
}

export interface PaiementCreateDTO {
  montant: number;
  date: string;
  modePaiement: string;
  inscriptionId: number;
  factureId: number;
}

export interface PaiementUpdateDTO {
  montant?: number;
  date?: string;
  modePaiement?: string;
  inscriptionId?: number;
  factureId?: number;
}

export interface PaiementResponseDTO {
  id: number;
  montant: number;
  date: string;
  modePaiement: string;
  inscriptionId?: number;
  factureId?: number;
}
