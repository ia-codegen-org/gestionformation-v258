package com.gestionformation.formation;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.security.SecurityUtils;
import com.gestionformation.categorie.Categorie;
import com.gestionformation.categorie.CategorieRepository;
import com.gestionformation.formateur.Formateur;
import com.gestionformation.formateur.FormateurRepository;
import com.gestionformation.formateur.FormateurResponseDTO;
import com.gestionformation.formateur.FormateurMapper;
import com.gestionformation.session.Session;
import com.gestionformation.session.SessionRepository;
import com.gestionformation.session.SessionResponseDTO;
import com.gestionformation.session.SessionMapper;
import com.gestionformation.evaluation.Evaluation;
import com.gestionformation.evaluation.EvaluationRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class FormationService {

    private static final String FORMATION_NOT_FOUND = "Formation not found with id: ";

    private final FormationRepository repository;
    private final FormationMapper     mapper;
    private final SecurityUtils securityUtils;
    private final CategorieRepository categorieRepository;
    private final FormateurRepository formateurRepository;
    private final FormateurMapper formateurMapper;
    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;
    private final EvaluationRepository evaluationRepository;

    private java.util.stream.Stream<Formation> accessibleStream() {
        if (securityUtils.isAdmin()) return repository.findAll().stream();
        final String currentUser = securityUtils.getConnectedUsername();
        if (currentUser == null) return java.util.stream.Stream.empty();
        return repository.findAll().stream()
                .filter(e ->
                    currentUser.equals(e.getCreatedBy())
                    || (e.getFormateurs() != null && e.getFormateurs().stream().anyMatch(a -> currentUser.equals(a.getUsername())))
                );
    }

    private void checkOwnership(Formation entity, String message) {
        if (securityUtils.isAdmin()) return;
        final String currentUser = securityUtils.getConnectedUsername();
        if (currentUser == null) return;
        if (!(entity.getCreatedBy() != null
                    && currentUser.equals(entity.getCreatedBy())
                    || (entity.getFormateurs() != null && entity.getFormateurs().stream().anyMatch(a -> currentUser.equals(a.getUsername()))))) {
            throw new org.springframework.security.access.AccessDeniedException(message);
        }
    }

    @Transactional(readOnly = true)
    public List<FormationResponseDTO> findAll() {
        return accessibleStream()
                .map(entity -> {
                    FormationResponseDTO dto = mapper.toResponseDTO(entity);
                    dto.setTauxReussite(calculateTauxReussite(entity.getId()));
                    return dto;
                }).toList();
    }

    @Transactional(readOnly = true)
    public long count() {
        return accessibleStream().count();
    }

    @Transactional(readOnly = true)
    public FormationResponseDTO findById(Long id) {
        Formation entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FORMATION_NOT_FOUND + id));
        checkOwnership(entity, "Accès refusé");
        FormationResponseDTO dto = mapper.toResponseDTO(entity);
        dto.setTauxReussite(calculateTauxReussite(id));
        return dto;
    }

    public FormationResponseDTO create(FormationCreateDTO dto) {
        Formation entity = mapper.toEntity(dto);
        if (!securityUtils.isAdmin()) {
            entity.setCreatedBy(securityUtils.getConnectedUsername());
        }
        return mapper.toResponseDTO(repository.save(entity));
    }

    public FormationResponseDTO update(Long id, FormationUpdateDTO dto) {
        Formation entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FORMATION_NOT_FOUND + id));
        checkOwnership(entity, "Vous ne pouvez modifier que vos propres données");
        mapper.updateEntity(entity, dto);
        return mapper.toResponseDTO(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        Formation entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FORMATION_NOT_FOUND + id));
        checkOwnership(entity, "Vous ne pouvez supprimer que vos propres données");
        formateurRepository.findAll().forEach(owner -> {
            if (owner.getFormations() != null) {
                owner.getFormations().removeIf(e -> e.getId().equals(id));
                formateurRepository.save(owner);
            }
        });
        if (entity.getSessions() != null && !entity.getSessions().isEmpty()) {
            sessionRepository
                    .deleteAll(entity.getSessions());
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<FormationResponseDTO> findByCategorieId(Long categorieId) {
        return repository.findByCategorieId(categorieId)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FormateurResponseDTO> findFormateursByFormationId(Long id) {
        Formation entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FORMATION_NOT_FOUND + id));
        return entity.getFormateurs()
                .stream()
                .map(formateurMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SessionResponseDTO> findSessionsByFormationId(Long id) {
        Formation entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FORMATION_NOT_FOUND + id));
        return entity.getSessions()
                .stream()
                .map(sessionMapper::toResponseDTO)
                .toList();
    }

    public Double calculateTauxReussite(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException(
                    FORMATION_NOT_FOUND + id);

        List<Session> step0 = sessionRepository.findByFormationId(id);
        if (step0 == null || step0.isEmpty()) return 0.0;

        return step0.stream()
                .flatMap(e -> {
                    List<Evaluation> sub = evaluationRepository.findBySessionId(e.getId());
                    return sub != null ? sub.stream() : java.util.stream.Stream.empty();
                })
                .filter(e -> e.getNote() != null)
                .mapToDouble(e -> e.getNote())
                .average().orElse(0.0);
    }
}
