package com.gestionformation.batiment;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gestionformation.exception.ResourceNotFoundException;

@Component
@RequiredArgsConstructor
public class BatimentMapper {



public BatimentResponseDTO toResponseDTO(Batiment entity) {
    if (entity == null) return null;
    return BatimentResponseDTO.builder()
            .id(entity.getId())
                .nom(entity.getNom())
                .adresse(entity.getAdresse())
                .build();
}


public Batiment toEntity(BatimentCreateDTO dto) {
    if (dto == null) return null;
    return Batiment.builder()
                .nom(dto.getNom())
                .adresse(dto.getAdresse())
                .build();
}


public void updateEntity(Batiment entity, BatimentUpdateDTO dto) {
    if (dto == null) return;
        entity.setNom(dto.getNom());
        entity.setAdresse(dto.getAdresse());
    }

}
