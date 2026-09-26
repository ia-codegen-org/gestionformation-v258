package com.gestionformation.formation;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FormationRepository extends JpaRepository<Formation, Long> {

    // ✅ Données privées (propriétaire) + données publiques (admin)
    List<Formation> findByCreatedByOrCreatedByIsNull(String createdBy);

    // Recherche par categorie
    List<Formation> findByCategorieId(Long categorieId);
    // Recherche par titre
    List<Formation> findByTitre(String titre);
    List<Formation> findByTitreContainingIgnoreCase(String titre);
    // Recherche par description
    List<Formation> findByDescription(String description);
    List<Formation> findByDescriptionContainingIgnoreCase(String description);
    // Recherche par dureeHeures
    List<Formation> findByDureeHeures(Integer dureeHeures);
    List<Formation> findByDureeHeuresLessThan(Integer dureeHeures);
    List<Formation> findByDureeHeuresGreaterThan(Integer dureeHeures);
}
