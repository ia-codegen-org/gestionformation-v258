package com.gestionformation.session;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.security.SecurityUtils;
import com.gestionformation.formation.Formation;
import com.gestionformation.formation.FormationRepository;
import com.gestionformation.formateur.Formateur;
import com.gestionformation.formateur.FormateurRepository;
import com.gestionformation.salle.Salle;
import com.gestionformation.salle.SalleRepository;
import com.gestionformation.etudiant.Etudiant;
import com.gestionformation.etudiant.EtudiantRepository;
import com.gestionformation.etudiant.EtudiantResponseDTO;
import com.gestionformation.etudiant.EtudiantMapper;
import com.gestionformation.inscription.Inscription;
import com.gestionformation.inscription.InscriptionRepository;
import com.gestionformation.inscription.InscriptionResponseDTO;
import com.gestionformation.inscription.InscriptionMapper;
import com.gestionformation.evaluation.Evaluation;
import com.gestionformation.evaluation.EvaluationRepository;
import com.gestionformation.evaluation.EvaluationResponseDTO;
import com.gestionformation.evaluation.EvaluationMapper;
import com.gestionformation.paiement.Paiement;
import com.gestionformation.paiement.PaiementRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class SessionService {

    private static final String SESSION_NOT_FOUND = "Session not found with id: ";

    private final SessionRepository repository;
    private final SessionMapper     mapper;
    private final SecurityUtils securityUtils;
    private final FormationRepository formationRepository;
    private final FormateurRepository formateurRepository;
    private final SalleRepository salleRepository;
    private final EtudiantRepository etudiantRepository;
    private final EtudiantMapper etudiantMapper;
    private final InscriptionRepository inscriptionRepository;
    private final InscriptionMapper inscriptionMapper;
    private final EvaluationRepository evaluationRepository;
    private final EvaluationMapper evaluationMapper;
    private final PaiementRepository paiementRepository;

    private java.util.stream.Stream<Session> accessibleStream() {
        if (securityUtils.isAdmin()) return repository.findAll().stream();
        final String currentUser = securityUtils.getConnectedUsername();
        if (currentUser == null) return java.util.stream.Stream.empty();
        return repository.findAll().stream()
                .filter(e ->
                    currentUser.equals(e.getCreatedBy())
                    || (e.getFormateur() != null && currentUser.equals(e.getFormateur().getUsername()))
                    || (e.getEtudiants() != null && e.getEtudiants().stream().anyMatch(a -> currentUser.equals(a.getUsername())))
                );
    }

    private void checkOwnership(Session entity, String message) {
        if (securityUtils.isAdmin()) return;
        final String currentUser = securityUtils.getConnectedUsername();
        if (currentUser == null) return;
        if (!(entity.getCreatedBy() != null
                    && currentUser.equals(entity.getCreatedBy())
                    || (entity.getFormateur() != null && currentUser.equals(entity.getFormateur().getUsername()))
                    || (entity.getEtudiants() != null && entity.getEtudiants().stream().anyMatch(a -> currentUser.equals(a.getUsername()))))) {
            throw new org.springframework.security.access.AccessDeniedException(message);
        }
    }

    @Transactional(readOnly = true)
    public List<SessionResponseDTO> findAll() {
        return accessibleStream()
                .map(entity -> {
                    SessionResponseDTO dto = mapper.toResponseDTO(entity);
                    dto.setCoutTotal(calculateCoutTotal(entity.getId()));
                    return dto;
                }).toList();
    }

    @Transactional(readOnly = true)
    public long count() {
        return accessibleStream().count();
    }

    @Transactional(readOnly = true)
    public SessionResponseDTO findById(Long id) {
        Session entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        SESSION_NOT_FOUND + id));
        checkOwnership(entity, "Accès refusé");
        SessionResponseDTO dto = mapper.toResponseDTO(entity);
        dto.setCoutTotal(calculateCoutTotal(id));
        return dto;
    }

    public SessionResponseDTO create(SessionCreateDTO dto) {
        Session entity = mapper.toEntity(dto);
        if (!securityUtils.isAdmin()) {
            entity.setCreatedBy(securityUtils.getConnectedUsername());
        }
        return mapper.toResponseDTO(repository.save(entity));
    }

    public SessionResponseDTO update(Long id, SessionUpdateDTO dto) {
        Session entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        SESSION_NOT_FOUND + id));
        checkOwnership(entity, "Vous ne pouvez modifier que vos propres données");
        mapper.updateEntity(entity, dto);
        return mapper.toResponseDTO(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        Session entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        SESSION_NOT_FOUND + id));
        checkOwnership(entity, "Vous ne pouvez supprimer que vos propres données");
        etudiantRepository.findAll().forEach(owner -> {
            if (owner.getSessions() != null) {
                owner.getSessions().removeIf(e -> e.getId().equals(id));
                etudiantRepository.save(owner);
            }
        });
        if (entity.getInscriptions() != null && !entity.getInscriptions().isEmpty()) {
            inscriptionRepository
                    .deleteAll(entity.getInscriptions());
        }
        if (entity.getEvaluations() != null && !entity.getEvaluations().isEmpty()) {
            evaluationRepository
                    .deleteAll(entity.getEvaluations());
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<SessionResponseDTO> findByFormationId(Long formationId) {
        return repository.findByFormationId(formationId)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SessionResponseDTO> findByFormateurId(Long formateurId) {
        return repository.findByFormateurId(formateurId)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SessionResponseDTO> findBySalleId(Long salleId) {
        return repository.findBySalleId(salleId)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EtudiantResponseDTO> findEtudiantsBySessionId(Long id) {
        Session entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        SESSION_NOT_FOUND + id));
        return entity.getEtudiants()
                .stream()
                .map(etudiantMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<InscriptionResponseDTO> findInscriptionsBySessionId(Long id) {
        Session entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        SESSION_NOT_FOUND + id));
        return entity.getInscriptions()
                .stream()
                .map(inscriptionMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<EvaluationResponseDTO> findEvaluationsBySessionId(Long id) {
        Session entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        SESSION_NOT_FOUND + id));
        return entity.getEvaluations()
                .stream()
                .map(evaluationMapper::toResponseDTO)
                .toList();
    }

    public Double calculateCoutTotal(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException(
                    SESSION_NOT_FOUND + id);

        List<Inscription> step0 = inscriptionRepository.findBySessionId(id);
        if (step0 == null || step0.isEmpty()) return 0.0;

        return step0.stream()
                .flatMap(e -> {
                    List<Paiement> sub = paiementRepository.findByInscriptionId(e.getId());
                    return sub != null ? sub.stream() : java.util.stream.Stream.empty();
                })
                .filter(e -> e.getMontant() != null)
                .mapToDouble(e -> e.getMontant())
                .sum();
    }
}
