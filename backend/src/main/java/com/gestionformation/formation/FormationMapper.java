package com.gestionformation.formation;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.categorie.Categorie;
import com.gestionformation.categorie.CategorieRepository;

@Component
@RequiredArgsConstructor
public class FormationMapper {

    private final CategorieRepository categorieRepository;


public FormationResponseDTO toResponseDTO(Formation entity) {
    if (entity == null) return null;
    return FormationResponseDTO.builder()
            .id(entity.getId())
                .createdBy(entity.getCreatedBy())
                .titre(entity.getTitre())
                .description(entity.getDescription())
                .dureeHeures(entity.getDureeHeures())
                .categorieId(entity.getCategorie() != null ? entity.getCategorie().getId() : null)
                .formateursUsernames(entity.getFormateurs() != null
                        ? entity.getFormateurs().stream()
                                .map(u -> u.getUsername())
                                .collect(java.util.stream.Collectors.toList())
                        : java.util.List.of())
                .build();
}


public Formation toEntity(FormationCreateDTO dto) {
    if (dto == null) return null;
    return Formation.builder()
                .titre(dto.getTitre())
                .description(dto.getDescription())
                .dureeHeures(dto.getDureeHeures())
        .categorie(dto.getCategorieId() != null
                ? categorieRepository.findById(dto.getCategorieId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Categorie not found with id: " + dto.getCategorieId()))
                : null)
                .build();
}


public void updateEntity(Formation entity, FormationUpdateDTO dto) {
    if (dto == null) return;
        entity.setTitre(dto.getTitre());
        entity.setDescription(dto.getDescription());
        entity.setDureeHeures(dto.getDureeHeures());
if (dto.getCategorieId() != null) {
    entity.setCategorie(categorieRepository.findById(dto.getCategorieId())
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Categorie not found with id: " + dto.getCategorieId())));
}
    }

}
