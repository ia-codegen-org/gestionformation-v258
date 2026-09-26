package com.gestionformation.evaluation;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    // ✅ Données privées (propriétaire) + données publiques (admin)
    List<Evaluation> findByCreatedByOrCreatedByIsNull(String createdBy);

    // Recherche par etudiant
    List<Evaluation> findByEtudiantId(Long etudiantId);
    // Recherche par session
    List<Evaluation> findBySessionId(Long sessionId);
    // Recherche par note
    List<Evaluation> findByNote(Double note);
    List<Evaluation> findByNoteLessThan(Double note);
    List<Evaluation> findByNoteGreaterThan(Double note);
    // Recherche par commentaire
    List<Evaluation> findByCommentaire(String commentaire);
    List<Evaluation> findByCommentaireContainingIgnoreCase(String commentaire);
}
