package com.gestionformation.formateurinterne;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.security.Role;

@Component
@RequiredArgsConstructor
public class FormateurInterneMapper {



public FormateurInterneResponseDTO toResponseDTO(FormateurInterne entity) {
    if (entity == null) return null;
    return FormateurInterneResponseDTO.builder()
            .id(entity.getId())
            .username(entity.getUsername())
            .email(entity.getEmail())
            .role(entity.getRole().name())
            .enabled(entity.isEnabled())
                .nom(entity.getNom())
                .prenom(entity.getPrenom())
                .anneesExperience(entity.getAnneesExperience())
                .salaireMensuel(entity.getSalaireMensuel())
                .build();
}


public FormateurInterne toEntity(FormateurInterneCreateDTO dto) {
    if (dto == null) return null;
    return FormateurInterne.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .role(Role.FORMATEUR)
                .mustChangePassword(true)
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .anneesExperience(dto.getAnneesExperience())
                .salaireMensuel(dto.getSalaireMensuel())
                    .build();
}


public void updateEntity(FormateurInterne entity, FormateurInterneUpdateDTO dto) {
    if (dto == null) return;
        if (dto.getUsername() != null) entity.setUsername(dto.getUsername());
        if (dto.getEmail()    != null) entity.setEmail(dto.getEmail());
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        entity.setAnneesExperience(dto.getAnneesExperience());
        entity.setSalaireMensuel(dto.getSalaireMensuel());
    }

}
