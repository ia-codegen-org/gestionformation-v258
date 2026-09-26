package com.gestionformation.categorie;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CategorieRepository extends JpaRepository<Categorie, Long> {

    // ✅ Données privées (propriétaire) + données publiques (admin)
    List<Categorie> findByCreatedByOrCreatedByIsNull(String createdBy);

    // Recherche par nom
    List<Categorie> findByNom(String nom);
    List<Categorie> findByNomContainingIgnoreCase(String nom);
    // Recherche par description
    List<Categorie> findByDescription(String description);
    List<Categorie> findByDescriptionContainingIgnoreCase(String description);
}
