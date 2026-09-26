package com.gestionformation.etudiant;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Date;

public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    // Recherche par nom
    List<Etudiant> findByNom(String nom);
    List<Etudiant> findByNomContainingIgnoreCase(String nom);
    // Recherche par prenom
    List<Etudiant> findByPrenom(String prenom);
    List<Etudiant> findByPrenomContainingIgnoreCase(String prenom);
    // Recherche par dateNaissance
    List<Etudiant> findByDateNaissance(Date dateNaissance);
    List<Etudiant> findByDateNaissanceBetween(Date start, Date end);
}
