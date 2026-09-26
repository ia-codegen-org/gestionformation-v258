package com.gestionformation.formateur;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FormateurRepository extends JpaRepository<Formateur, Long> {

    // Recherche par departement
    List<Formateur> findByDepartementId(Long departementId);
    // Recherche par nom
    List<Formateur> findByNom(String nom);
    List<Formateur> findByNomContainingIgnoreCase(String nom);
    // Recherche par prenom
    List<Formateur> findByPrenom(String prenom);
    List<Formateur> findByPrenomContainingIgnoreCase(String prenom);
    // Recherche par anneesExperience
    List<Formateur> findByAnneesExperience(Integer anneesExperience);
    List<Formateur> findByAnneesExperienceLessThan(Integer anneesExperience);
    List<Formateur> findByAnneesExperienceGreaterThan(Integer anneesExperience);
}
