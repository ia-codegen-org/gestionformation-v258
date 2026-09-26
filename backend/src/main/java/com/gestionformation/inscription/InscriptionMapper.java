package com.gestionformation.inscription;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.etudiant.Etudiant;
import com.gestionformation.etudiant.EtudiantRepository;
import com.gestionformation.session.Session;
import com.gestionformation.session.SessionRepository;
import com.gestionformation.paiement.Paiement;
import com.gestionformation.paiement.PaiementRepository;

@Component
@RequiredArgsConstructor
public class InscriptionMapper {

    private final EtudiantRepository etudiantRepository;
    private final SessionRepository sessionRepository;
    private final PaiementRepository paiementRepository;


public InscriptionResponseDTO toResponseDTO(Inscription entity) {
    if (entity == null) return null;
    return InscriptionResponseDTO.builder()
            .id(entity.getId())
                .createdBy(entity.getCreatedBy())
                .statut(entity.getStatut())
                .dateInscription(entity.getDateInscription())
                .etudiantId(entity.getEtudiant() != null ? entity.getEtudiant().getId() : null)
                .sessionId(entity.getSession() != null ? entity.getSession().getId() : null)
                .paiementId(entity.getPaiement() != null ? entity.getPaiement().getId() : null)
                .build();
}


public Inscription toEntity(InscriptionCreateDTO dto) {
    if (dto == null) return null;
    return Inscription.builder()
                .statut(dto.getStatut())
                .dateInscription(dto.getDateInscription())
        .etudiant(dto.getEtudiantId() != null
                ? etudiantRepository.findById(dto.getEtudiantId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Etudiant not found with id: " + dto.getEtudiantId()))
                : null)
        .session(dto.getSessionId() != null
                ? sessionRepository.findById(dto.getSessionId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Session not found with id: " + dto.getSessionId()))
                : null)
        .paiement(dto.getPaiementId() != null
                ? paiementRepository.findById(dto.getPaiementId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Paiement not found with id: " + dto.getPaiementId()))
                : null)
                .build();
}


public void updateEntity(Inscription entity, InscriptionUpdateDTO dto) {
    if (dto == null) return;
        entity.setStatut(dto.getStatut());
        entity.setDateInscription(dto.getDateInscription());
if (dto.getEtudiantId() != null) {
    entity.setEtudiant(etudiantRepository.findById(dto.getEtudiantId())
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Etudiant not found with id: " + dto.getEtudiantId())));
}
if (dto.getSessionId() != null) {
    entity.setSession(sessionRepository.findById(dto.getSessionId())
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Session not found with id: " + dto.getSessionId())));
}
if (dto.getPaiementId() != null) {
    entity.setPaiement(paiementRepository.findById(dto.getPaiementId())
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Paiement not found with id: " + dto.getPaiementId())));
}
    }

}
