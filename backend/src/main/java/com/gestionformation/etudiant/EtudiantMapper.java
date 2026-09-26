package com.gestionformation.etudiant;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.security.Role;

@Component
@RequiredArgsConstructor
public class EtudiantMapper {



public EtudiantResponseDTO toResponseDTO(Etudiant entity) {
    if (entity == null) return null;
    return EtudiantResponseDTO.builder()
            .id(entity.getId())
            .username(entity.getUsername())
            .email(entity.getEmail())
            .role(entity.getRole().name())
            .enabled(entity.isEnabled())
                .nom(entity.getNom())
                .prenom(entity.getPrenom())
                .dateNaissance(entity.getDateNaissance())
                .noteMoyenne(entity.getNoteMoyenne())
                .nbCertificats(entity.getNbCertificats())
                .build();
}


public Etudiant toEntity(EtudiantCreateDTO dto) {
    if (dto == null) return null;
    return Etudiant.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .role(Role.ETUDIANT)
                .mustChangePassword(true)
                .nom(dto.getNom())
                .prenom(dto.getPrenom())
                .dateNaissance(dto.getDateNaissance())
                    .build();
}


public void updateEntity(Etudiant entity, EtudiantUpdateDTO dto) {
    if (dto == null) return;
        if (dto.getUsername() != null) entity.setUsername(dto.getUsername());
        if (dto.getEmail()    != null) entity.setEmail(dto.getEmail());
        entity.setNom(dto.getNom());
        entity.setPrenom(dto.getPrenom());
        entity.setDateNaissance(dto.getDateNaissance());
    }

}
