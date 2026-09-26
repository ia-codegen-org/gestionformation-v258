package com.gestionformation.session;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.formation.Formation;
import com.gestionformation.formation.FormationRepository;
import com.gestionformation.formateur.Formateur;
import com.gestionformation.formateur.FormateurRepository;
import com.gestionformation.salle.Salle;
import com.gestionformation.salle.SalleRepository;

@Component
@RequiredArgsConstructor
public class SessionMapper {

    private final FormationRepository formationRepository;
    private final FormateurRepository formateurRepository;
    private final SalleRepository salleRepository;


public SessionResponseDTO toResponseDTO(Session entity) {
    if (entity == null) return null;
    return SessionResponseDTO.builder()
            .id(entity.getId())
                .createdBy(entity.getCreatedBy())
                .dateDebut(entity.getDateDebut())
                .dateFin(entity.getDateFin())
                .nbEtudiants(entity.getNbEtudiants())
                .formationId(entity.getFormation() != null ? entity.getFormation().getId() : null)
                .formateurId(entity.getFormateur() != null ? entity.getFormateur().getId() : null)
                .salleId(entity.getSalle() != null ? entity.getSalle().getId() : null)
                .etudiantsUsernames(entity.getEtudiants() != null
                        ? entity.getEtudiants().stream()
                                .map(u -> u.getUsername())
                                .collect(java.util.stream.Collectors.toList())
                        : java.util.List.of())
                .build();
}


public Session toEntity(SessionCreateDTO dto) {
    if (dto == null) return null;
    return Session.builder()
                .dateDebut(dto.getDateDebut())
                .dateFin(dto.getDateFin())
        .formation(dto.getFormationId() != null
                ? formationRepository.findById(dto.getFormationId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Formation not found with id: " + dto.getFormationId()))
                : null)
        .formateur(dto.getFormateurId() != null
                ? formateurRepository.findById(dto.getFormateurId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Formateur not found with id: " + dto.getFormateurId()))
                : null)
        .salle(dto.getSalleId() != null
                ? salleRepository.findById(dto.getSalleId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Salle not found with id: " + dto.getSalleId()))
                : null)
                .build();
}


public void updateEntity(Session entity, SessionUpdateDTO dto) {
    if (dto == null) return;
        entity.setDateDebut(dto.getDateDebut());
        entity.setDateFin(dto.getDateFin());
if (dto.getFormationId() != null) {
    entity.setFormation(formationRepository.findById(dto.getFormationId())
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Formation not found with id: " + dto.getFormationId())));
}
if (dto.getFormateurId() != null) {
    entity.setFormateur(formateurRepository.findById(dto.getFormateurId())
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Formateur not found with id: " + dto.getFormateurId())));
}
if (dto.getSalleId() != null) {
    entity.setSalle(salleRepository.findById(dto.getSalleId())
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Salle not found with id: " + dto.getSalleId())));
}
    }

}
