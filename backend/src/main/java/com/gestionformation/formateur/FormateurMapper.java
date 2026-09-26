package com.gestionformation.formateur;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.security.Role;
import com.gestionformation.departement.Departement;
import com.gestionformation.departement.DepartementRepository;

@Component
@RequiredArgsConstructor
public class FormateurMapper {

    private final DepartementRepository departementRepository;


public FormateurResponseDTO toResponseDTO(Formateur entity) {
    if (entity == null) return null;
    return FormateurResponseDTO.builder()
            .id(entity.getId())
            .username(entity.getUsername())
            .email(entity.getEmail())
            .role(entity.getRole().name())
            .enabled(entity.isEnabled())
                .nom(entity.getNom())
                .prenom(entity.getPrenom())
                .anneesExperience(entity.getAnneesExperience())
                .nbSessions(entity.getNbSessions())
                .departementId(entity.getDepartement() != null ? entity.getDepartement().getId() : null)
                .build();
}


public Formateur toEntity(FormateurCreateDTO dto) {
    if (dto == null) return null;
    return Formateur.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .role(Role.FORMATEUR)
                .mustChangePassword(true)
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .anneesExperience(dto.getAnneesExperience())
        .departement(dto.getDepartementId() != null
                ? departementRepository.findById(dto.getDepartementId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Departement not found with id: " + dto.getDepartementId()))
                : null)
                    .build();
}


public void updateEntity(Formateur entity, FormateurUpdateDTO dto) {
    if (dto == null) return;
        if (dto.getUsername() != null) entity.setUsername(dto.getUsername());
        if (dto.getEmail()    != null) entity.setEmail(dto.getEmail());
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        entity.setAnneesExperience(dto.getAnneesExperience());
if (dto.getDepartementId() != null) {
    entity.setDepartement(departementRepository.findById(dto.getDepartementId())
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Departement not found with id: " + dto.getDepartementId())));
}
    }

}
