package com.gestionformation.salle;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.batiment.Batiment;
import com.gestionformation.batiment.BatimentRepository;

@Component
@RequiredArgsConstructor
public class SalleMapper {

    private final BatimentRepository batimentRepository;


public SalleResponseDTO toResponseDTO(Salle entity) {
    if (entity == null) return null;
    return SalleResponseDTO.builder()
            .id(entity.getId())
                .createdBy(entity.getCreatedBy())
                .numero(entity.getNumero())
                .capaciteMax(entity.getCapaciteMax())
                .equipement(entity.getEquipement())
                .batimentId(entity.getBatiment() != null ? entity.getBatiment().getId() : null)
                .build();
}


public Salle toEntity(SalleCreateDTO dto) {
    if (dto == null) return null;
    return Salle.builder()
                .numero(dto.getNumero())
                .capaciteMax(dto.getCapaciteMax())
                .equipement(dto.getEquipement())
        .batiment(dto.getBatimentId() != null
                ? batimentRepository.findById(dto.getBatimentId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Batiment not found with id: " + dto.getBatimentId()))
                : null)
                .build();
}


public void updateEntity(Salle entity, SalleUpdateDTO dto) {
    if (dto == null) return;
        entity.setNumero(dto.getNumero());
        entity.setCapaciteMax(dto.getCapaciteMax());
        entity.setEquipement(dto.getEquipement());
if (dto.getBatimentId() != null) {
    entity.setBatiment(batimentRepository.findById(dto.getBatimentId())
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Batiment not found with id: " + dto.getBatimentId())));
}
    }

}
