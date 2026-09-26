// ─── Modèle généré automatiquement par IACodeGen

export interface Evaluation {
  id?: number;
  note: number;
  commentaire: string;
  etudiant?: { id: number };
  session?: { id: number };
}

export interface EvaluationCreateDTO {
  note: number;
  commentaire: string;
  etudiantId: number;
  sessionId: number;
}

export interface EvaluationUpdateDTO {
  note?: number;
  commentaire?: string;
  etudiantId?: number;
  sessionId?: number;
}

export interface EvaluationResponseDTO {
  id: number;
  note: number;
  commentaire: string;
  etudiantId?: number;
  sessionId?: number;
}
