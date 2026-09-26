package com.gestionformation.formateurexterne;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FormateurExterneRepository extends JpaRepository<FormateurExterne, Long> {

    // Recherche par tarifJournalier
    List<FormateurExterne> findByTarifJournalier(Double tarifJournalier);
    List<FormateurExterne> findByTarifJournalierLessThan(Double tarifJournalier);
    List<FormateurExterne> findByTarifJournalierGreaterThan(Double tarifJournalier);
}
