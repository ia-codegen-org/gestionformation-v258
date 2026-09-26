package com.gestionformation.facture;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Date;

public interface FactureRepository extends JpaRepository<Facture, Long> {

    // Vérification d'unicité — numero
    boolean existsByNumero(String numero);
    boolean existsByNumeroAndIdNot(String numero, Long id);
    // ✅ Données privées (propriétaire) + données publiques (admin)
    List<Facture> findByCreatedByOrCreatedByIsNull(String createdBy);

    // Recherche par paiement
    List<Facture> findByPaiementId(Long paiementId);
    // Recherche par numero
    List<Facture> findByNumero(String numero);
    List<Facture> findByNumeroContainingIgnoreCase(String numero);
    // Recherche par date
    List<Facture> findByDate(Date date);
    List<Facture> findByDateBetween(Date start, Date end);
    // Recherche par montantTotal
    List<Facture> findByMontantTotal(Double montantTotal);
    List<Facture> findByMontantTotalLessThan(Double montantTotal);
    List<Facture> findByMontantTotalGreaterThan(Double montantTotal);
    // Recherche par statut
    List<Facture> findByStatut(String statut);
    List<Facture> findByStatutContainingIgnoreCase(String statut);
}
