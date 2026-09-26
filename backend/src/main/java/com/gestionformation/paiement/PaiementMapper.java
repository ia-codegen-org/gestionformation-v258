package com.gestionformation.paiement;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gestionformation.exception.ResourceNotFoundException;

@Component
@RequiredArgsConstructor
public class PaiementMapper {



public PaiementResponseDTO toResponseDTO(Paiement entity) {
    if (entity == null) return null;
    return PaiementResponseDTO.builder()
            .id(entity.getId())
                .createdBy(entity.getCreatedBy())
                .montant(entity.getMontant())
                .date(entity.getDate())
                .modePaiement(entity.getModePaiement())
                .build();
}


public Paiement toEntity(PaiementCreateDTO dto) {
    if (dto == null) return null;
    return Paiement.builder()
                .montant(dto.getMontant())
                .date(dto.getDate())
                .modePaiement(dto.getModePaiement())
                .build();
}


public void updateEntity(Paiement entity, PaiementUpdateDTO dto) {
    if (dto == null) return;
        entity.setMontant(dto.getMontant());
        entity.setDate(dto.getDate());
        entity.setModePaiement(dto.getModePaiement());
    }

}
