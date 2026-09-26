package com.gestionformation.facture;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.paiement.Paiement;
import com.gestionformation.paiement.PaiementRepository;

@Component
@RequiredArgsConstructor
public class FactureMapper {

    private final PaiementRepository paiementRepository;


public FactureResponseDTO toResponseDTO(Facture entity) {
    if (entity == null) return null;
    return FactureResponseDTO.builder()
            .id(entity.getId())
                .createdBy(entity.getCreatedBy())
                .numero(entity.getNumero())
                .date(entity.getDate())
                .montantTotal(entity.getMontantTotal())
                .statut(entity.getStatut())
                .paiementId(entity.getPaiement() != null ? entity.getPaiement().getId() : null)
                .build();
}


public Facture toEntity(FactureCreateDTO dto) {
    if (dto == null) return null;
    return Facture.builder()
                .numero(dto.getNumero())
                .date(dto.getDate())
                .montantTotal(dto.getMontantTotal())
                .statut(dto.getStatut())
        .paiement(dto.getPaiementId() != null
                ? paiementRepository.findById(dto.getPaiementId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Paiement not found with id: " + dto.getPaiementId()))
                : null)
                .build();
}


public void updateEntity(Facture entity, FactureUpdateDTO dto) {
    if (dto == null) return;
        entity.setNumero(dto.getNumero());
        entity.setDate(dto.getDate());
        entity.setMontantTotal(dto.getMontantTotal());
        entity.setStatut(dto.getStatut());
if (dto.getPaiementId() != null) {
    entity.setPaiement(paiementRepository.findById(dto.getPaiementId())
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Paiement not found with id: " + dto.getPaiementId())));
}
    }

}
