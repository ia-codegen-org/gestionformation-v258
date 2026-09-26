package com.gestionformation.paiement;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Date;

public interface PaiementRepository extends JpaRepository<Paiement, Long> {

    // ✅ Données privées (propriétaire) + données publiques (admin)
    List<Paiement> findByCreatedByOrCreatedByIsNull(String createdBy);

    // Recherche par inscription
    List<Paiement> findByInscriptionId(Long inscriptionId);
    // Recherche par facture
    List<Paiement> findByFactureId(Long factureId);
    // Recherche par montant
    List<Paiement> findByMontant(Double montant);
    List<Paiement> findByMontantLessThan(Double montant);
    List<Paiement> findByMontantGreaterThan(Double montant);
    // Recherche par date
    List<Paiement> findByDate(Date date);
    List<Paiement> findByDateBetween(Date start, Date end);
    // Recherche par modePaiement
    List<Paiement> findByModePaiement(String modePaiement);
    List<Paiement> findByModePaiementContainingIgnoreCase(String modePaiement);
}
