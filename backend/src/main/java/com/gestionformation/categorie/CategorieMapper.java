package com.gestionformation.categorie;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gestionformation.exception.ResourceNotFoundException;

@Component
@RequiredArgsConstructor
public class CategorieMapper {



public CategorieResponseDTO toResponseDTO(Categorie entity) {
    if (entity == null) return null;
    return CategorieResponseDTO.builder()
            .id(entity.getId())
                .createdBy(entity.getCreatedBy())
                .nom(entity.getNom())
                .description(entity.getDescription())
                .build();
}


public Categorie toEntity(CategorieCreateDTO dto) {
    if (dto == null) return null;
    return Categorie.builder()
                .nom(dto.getNom())
                .description(dto.getDescription())
                .build();
}


public void updateEntity(Categorie entity, CategorieUpdateDTO dto) {
    if (dto == null) return;
        entity.setNom(dto.getNom());
        entity.setDescription(dto.getDescription());
    }

}
