package com.gestionformation.departement;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gestionformation.exception.ResourceNotFoundException;

@Component
@RequiredArgsConstructor
public class DepartementMapper {



public DepartementResponseDTO toResponseDTO(Departement entity) {
    if (entity == null) return null;
    return DepartementResponseDTO.builder()
            .id(entity.getId())
                .createdBy(entity.getCreatedBy())
                .nom(entity.getNom())
                .budget(entity.getBudget())
                .build();
}


public Departement toEntity(DepartementCreateDTO dto) {
    if (dto == null) return null;
    return Departement.builder()
                .nom(dto.getNom())
                .budget(dto.getBudget())
                .build();
}


public void updateEntity(Departement entity, DepartementUpdateDTO dto) {
    if (dto == null) return;
        entity.setNom(dto.getNom());
        entity.setBudget(dto.getBudget());
    }

}
