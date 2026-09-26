package com.gestionformation.formateurinterne;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FormateurInterneRepository extends JpaRepository<FormateurInterne, Long> {

    // Recherche par salaireMensuel
    List<FormateurInterne> findBySalaireMensuel(Double salaireMensuel);
    List<FormateurInterne> findBySalaireMensuelLessThan(Double salaireMensuel);
    List<FormateurInterne> findBySalaireMensuelGreaterThan(Double salaireMensuel);
}
