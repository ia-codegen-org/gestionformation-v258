package com.gestionformation.evaluation;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.etudiant.Etudiant;
import com.gestionformation.etudiant.EtudiantRepository;
import com.gestionformation.session.Session;
import com.gestionformation.session.SessionRepository;

@Component
@RequiredArgsConstructor
public class EvaluationMapper {

    private final EtudiantRepository etudiantRepository;
    private final SessionRepository sessionRepository;


public EvaluationResponseDTO toResponseDTO(Evaluation entity) {
    if (entity == null) return null;
    return EvaluationResponseDTO.builder()
            .id(entity.getId())
                .createdBy(entity.getCreatedBy())
                .note(entity.getNote())
                .commentaire(entity.getCommentaire())
                .etudiantId(entity.getEtudiant() != null ? entity.getEtudiant().getId() : null)
                .sessionId(entity.getSession() != null ? entity.getSession().getId() : null)
                .build();
}


public Evaluation toEntity(EvaluationCreateDTO dto) {
    if (dto == null) return null;
    return Evaluation.builder()
                .note(dto.getNote())
                .commentaire(dto.getCommentaire())
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
                .build();
}


public void updateEntity(Evaluation entity, EvaluationUpdateDTO dto) {
    if (dto == null) return;
        entity.setNote(dto.getNote());
        entity.setCommentaire(dto.getCommentaire());
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
    }

}
