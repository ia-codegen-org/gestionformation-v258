package com.gestionformation.salle;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SalleRepository extends JpaRepository<Salle, Long> {

    // Vérification d'unicité — numero
    boolean existsByNumero(String numero);
    boolean existsByNumeroAndIdNot(String numero, Long id);
    // ✅ Données privées (propriétaire) + données publiques (admin)
    List<Salle> findByCreatedByOrCreatedByIsNull(String createdBy);

    // Recherche par batiment
    List<Salle> findByBatimentId(Long batimentId);
    // Recherche par numero
    List<Salle> findByNumero(String numero);
    List<Salle> findByNumeroContainingIgnoreCase(String numero);
    // Recherche par capaciteMax
    List<Salle> findByCapaciteMax(Integer capaciteMax);
    List<Salle> findByCapaciteMaxLessThan(Integer capaciteMax);
    List<Salle> findByCapaciteMaxGreaterThan(Integer capaciteMax);
    // Recherche par equipement
    List<Salle> findByEquipement(String equipement);
    List<Salle> findByEquipementContainingIgnoreCase(String equipement);
}
