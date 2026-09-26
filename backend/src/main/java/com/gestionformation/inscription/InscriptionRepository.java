package com.gestionformation.inscription;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Date;

public interface InscriptionRepository extends JpaRepository<Inscription, Long> {

    // ✅ Données privées (propriétaire) + données publiques (admin)
    List<Inscription> findByCreatedByOrCreatedByIsNull(String createdBy);

    // Recherche par etudiant
    List<Inscription> findByEtudiantId(Long etudiantId);
    // Recherche par session
    List<Inscription> findBySessionId(Long sessionId);
    // Recherche par paiement
    List<Inscription> findByPaiementId(Long paiementId);
    // Recherche par statut
    List<Inscription> findByStatut(String statut);
    List<Inscription> findByStatutContainingIgnoreCase(String statut);
    // Recherche par dateInscription
    List<Inscription> findByDateInscription(Date dateInscription);
    List<Inscription> findByDateInscriptionBetween(Date start, Date end);
}
