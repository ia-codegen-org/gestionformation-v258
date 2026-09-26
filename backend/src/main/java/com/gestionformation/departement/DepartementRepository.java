package com.gestionformation.departement;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DepartementRepository extends JpaRepository<Departement, Long> {

    // ✅ Données privées (propriétaire) + données publiques (admin)
    List<Departement> findByCreatedByOrCreatedByIsNull(String createdBy);

    // Recherche par nom
    List<Departement> findByNom(String nom);
    List<Departement> findByNomContainingIgnoreCase(String nom);
    // Recherche par budget
    List<Departement> findByBudget(Double budget);
    List<Departement> findByBudgetLessThan(Double budget);
    List<Departement> findByBudgetGreaterThan(Double budget);
}
