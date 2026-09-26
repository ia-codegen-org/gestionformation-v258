package com.gestionformation.certificat;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Date;

public interface CertificatRepository extends JpaRepository<Certificat, Long> {

    // ✅ Données privées (propriétaire) + données publiques (admin)
    List<Certificat> findByCreatedByOrCreatedByIsNull(String createdBy);

    // Recherche par etudiant
    List<Certificat> findByEtudiantId(Long etudiantId);
    // Recherche par titre
    List<Certificat> findByTitre(String titre);
    List<Certificat> findByTitreContainingIgnoreCase(String titre);
    // Recherche par dateObtention
    List<Certificat> findByDateObtention(Date dateObtention);
    List<Certificat> findByDateObtentionBetween(Date start, Date end);
    // Recherche par mention
    List<Certificat> findByMention(String mention);
    List<Certificat> findByMentionContainingIgnoreCase(String mention);
}
