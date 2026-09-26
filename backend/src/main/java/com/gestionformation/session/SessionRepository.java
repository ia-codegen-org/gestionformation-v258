package com.gestionformation.session;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Date;

public interface SessionRepository extends JpaRepository<Session, Long> {

    // ✅ Données privées (propriétaire) + données publiques (admin)
    List<Session> findByCreatedByOrCreatedByIsNull(String createdBy);

    // Recherche par formation
    List<Session> findByFormationId(Long formationId);
    // Recherche par formateur
    List<Session> findByFormateurId(Long formateurId);
    // Recherche par salle
    List<Session> findBySalleId(Long salleId);
    // Recherche par dateDebut
    List<Session> findByDateDebut(Date dateDebut);
    List<Session> findByDateDebutBetween(Date start, Date end);
    // Recherche par dateFin
    List<Session> findByDateFin(Date dateFin);
    List<Session> findByDateFinBetween(Date start, Date end);
}
