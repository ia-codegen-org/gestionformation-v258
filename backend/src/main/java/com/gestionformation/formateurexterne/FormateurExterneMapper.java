package com.gestionformation.formateurexterne;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.security.Role;

@Component
@RequiredArgsConstructor
public class FormateurExterneMapper {



public FormateurExterneResponseDTO toResponseDTO(FormateurExterne entity) {
    if (entity == null) return null;
    return FormateurExterneResponseDTO.builder()
            .id(entity.getId())
            .username(entity.getUsername())
            .email(entity.getEmail())
            .role(entity.getRole().name())
            .enabled(entity.isEnabled())
                .nom(entity.getNom())
                .prenom(entity.getPrenom())
                .anneesExperience(entity.getAnneesExperience())
                .tarifJournalier(entity.getTarifJournalier())
                .build();
}


public FormateurExterne toEntity(FormateurExterneCreateDTO dto) {
    if (dto == null) return null;
    return FormateurExterne.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .role(Role.FORMATEUR)
                .mustChangePassword(true)
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .anneesExperience(dto.getAnneesExperience())
                .tarifJournalier(dto.getTarifJournalier())
                    .build();
}


public void updateEntity(FormateurExterne entity, FormateurExterneUpdateDTO dto) {
    if (dto == null) return;
        if (dto.getUsername() != null) entity.setUsername(dto.getUsername());
        if (dto.getEmail()    != null) entity.setEmail(dto.getEmail());
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        entity.setAnneesExperience(dto.getAnneesExperience());
        entity.setTarifJournalier(dto.getTarifJournalier());
    }

}
