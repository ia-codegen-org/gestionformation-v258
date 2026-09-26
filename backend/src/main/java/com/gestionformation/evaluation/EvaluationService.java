package com.gestionformation.evaluation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.security.SecurityUtils;
import com.gestionformation.etudiant.Etudiant;
import com.gestionformation.etudiant.EtudiantRepository;
import com.gestionformation.session.Session;
import com.gestionformation.session.SessionRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class EvaluationService {

    private static final String EVALUATION_NOT_FOUND = "Evaluation not found with id: ";

    private final EvaluationRepository repository;
    private final EvaluationMapper     mapper;
    private final SecurityUtils securityUtils;
    private final EtudiantRepository etudiantRepository;
    private final SessionRepository sessionRepository;

    private java.util.stream.Stream<Evaluation> accessibleStream() {
        if (securityUtils.isAdmin()) return repository.findAll().stream();
        final String currentUser = securityUtils.getConnectedUsername();
        if (currentUser == null) return java.util.stream.Stream.empty();
        return repository.findAll().stream()
                .filter(e ->
                    currentUser.equals(e.getCreatedBy())
                    || (e.getEtudiant() != null && currentUser.equals(e.getEtudiant().getUsername()))
                );
    }

    private void checkOwnership(Evaluation entity, String message) {
        if (securityUtils.isAdmin()) return;
        final String currentUser = securityUtils.getConnectedUsername();
        if (currentUser == null) return;
        if (!(entity.getCreatedBy() != null
                    && currentUser.equals(entity.getCreatedBy())
                    || (entity.getEtudiant() != null && currentUser.equals(entity.getEtudiant().getUsername())))) {
            throw new org.springframework.security.access.AccessDeniedException(message);
        }
    }

    @Transactional(readOnly = true)
    public List<EvaluationResponseDTO> findAll() {
        return accessibleStream()
                .map(mapper::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public long count() {
        return accessibleStream().count();
    }

    @Transactional(readOnly = true)
    public EvaluationResponseDTO findById(Long id) {
        Evaluation entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        EVALUATION_NOT_FOUND + id));
        checkOwnership(entity, "Accès refusé");
        EvaluationResponseDTO dto = mapper.toResponseDTO(entity);
        return dto;
    }

    public EvaluationResponseDTO create(EvaluationCreateDTO dto) {
        Evaluation entity = mapper.toEntity(dto);
        if (!securityUtils.isAdmin()) {
            entity.setCreatedBy(securityUtils.getConnectedUsername());
        }
        return mapper.toResponseDTO(repository.save(entity));
    }

    public EvaluationResponseDTO update(Long id, EvaluationUpdateDTO dto) {
        Evaluation entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        EVALUATION_NOT_FOUND + id));
        checkOwnership(entity, "Vous ne pouvez modifier que vos propres données");
        mapper.updateEntity(entity, dto);
        return mapper.toResponseDTO(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        Evaluation entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        EVALUATION_NOT_FOUND + id));
        checkOwnership(entity, "Vous ne pouvez supprimer que vos propres données");
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<EvaluationResponseDTO> findByEtudiantId(Long etudiantId) {
        return repository.findByEtudiantId(etudiantId)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EvaluationResponseDTO> findBySessionId(Long sessionId) {
        return repository.findBySessionId(sessionId)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }
}
