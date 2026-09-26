package com.gestionformation.etudiant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.security.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.gestionformation.security.EmailService;
import com.gestionformation.session.Session;
import com.gestionformation.session.SessionRepository;
import com.gestionformation.session.SessionResponseDTO;
import com.gestionformation.session.SessionMapper;
import com.gestionformation.evaluation.Evaluation;
import com.gestionformation.evaluation.EvaluationRepository;
import com.gestionformation.evaluation.EvaluationResponseDTO;
import com.gestionformation.evaluation.EvaluationMapper;
import com.gestionformation.certificat.Certificat;
import com.gestionformation.certificat.CertificatRepository;
import com.gestionformation.certificat.CertificatResponseDTO;
import com.gestionformation.certificat.CertificatMapper;
import com.gestionformation.inscription.Inscription;
import com.gestionformation.inscription.InscriptionRepository;
import com.gestionformation.inscription.InscriptionResponseDTO;
import com.gestionformation.inscription.InscriptionMapper;
import com.gestionformation.session.Session;

@Service
@Transactional
@RequiredArgsConstructor
public class EtudiantService {

    private static final String ETUDIANT_NOT_FOUND = "Etudiant not found with id: ";

    private static final java.security.SecureRandom RANDOM = new java.security.SecureRandom();

    private final EtudiantRepository repository;
    private final EtudiantMapper     mapper;
    private final SecurityUtils securityUtils;
    private final PasswordEncoder passwordEncoder;
    private final EmailService    emailService;
    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;
    private final EvaluationRepository evaluationRepository;
    private final EvaluationMapper evaluationMapper;
    private final CertificatRepository certificatRepository;
    private final CertificatMapper certificatMapper;
    private final InscriptionRepository inscriptionRepository;
    private final InscriptionMapper inscriptionMapper;

    @Transactional(readOnly = true)
    public List<EtudiantResponseDTO> findAll() {
        if (securityUtils.isAdmin()) {
            return repository.findAll().stream()
                    .map(entity -> {
                        EtudiantResponseDTO dto = mapper.toResponseDTO(entity);
                        dto.setDureeTotaleFormation(calculateDureeTotaleFormation(entity.getId()));
                        return dto;
                    }).toList();
        }

        String connectedRole = securityUtils.getConnectedRole();

        if ("ETUDIANT".equals(connectedRole)) {
            Long userId = securityUtils.getConnectedUserId();
            return repository.findById(userId)
                    .map(mapper::toResponseDTO)
                    .map(List::of)
                    .orElse(List.of());
        }

        return repository.findAll().stream()
                .map(mapper::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    @Transactional(readOnly = true)
    public EtudiantResponseDTO findById(Long id) {
        Etudiant entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ETUDIANT_NOT_FOUND + id));
        if (!securityUtils.isAdmin()) {
            String connectedRole = securityUtils.getConnectedRole();
            final String currentUser = securityUtils.getConnectedUsername();
            boolean hasAccess;
            if ("ETUDIANT".equals(connectedRole)) {
                hasAccess = id.equals(securityUtils.getConnectedUserId());
            }
            else if ("FORMATEUR".equals(connectedRole)) {
                hasAccess = true;
            }
            else {
                hasAccess = true;
            }
            if (!hasAccess) {
                throw new org.springframework.security.access.AccessDeniedException(
                        "Accès refusé");
            }
        }
        EtudiantResponseDTO dto = mapper.toResponseDTO(entity);
        dto.setDureeTotaleFormation(calculateDureeTotaleFormation(id));
        return dto;
    }

    public EtudiantResponseDTO create(EtudiantCreateDTO dto) {
        Etudiant entity = mapper.toEntity(dto);

        // ✅ Générer un mot de passe aléatoire sécurisé
        String generatedPassword = generateSecurePassword();
        entity.setPassword(passwordEncoder.encode(generatedPassword));
        entity.setMustChangePassword(true);
        entity.setEnabled(true);

        Etudiant saved = repository.save(entity);

        // ✅ Envoyer les identifiants par email
        emailService.sendCredentialsEmail(
                saved.getEmail(),
                saved.getUsername(),
                generatedPassword,
                "Etudiant"
        );

        return mapper.toResponseDTO(saved);
    }

    private String generateSecurePassword() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz23456789!@#$";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append(chars.charAt(RANDOM.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public EtudiantResponseDTO update(Long id, EtudiantUpdateDTO dto) {
        Etudiant entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ETUDIANT_NOT_FOUND + id));
        if (!securityUtils.isAdmin()) {
            String connectedRole = securityUtils.getConnectedRole();
            final String currentUser = securityUtils.getConnectedUsername();
            boolean hasAccess;
            if ("ETUDIANT".equals(connectedRole)) {
                hasAccess = id.equals(securityUtils.getConnectedUserId());
            }
            else if ("FORMATEUR".equals(connectedRole)) {
                hasAccess = true;
            }
            else {
                hasAccess = true;
            }
            if (!hasAccess) {
                throw new org.springframework.security.access.AccessDeniedException(
                        "Vous ne pouvez modifier que vos propres données");
            }
        }
        mapper.updateEntity(entity, dto);
        return mapper.toResponseDTO(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        Etudiant entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ETUDIANT_NOT_FOUND + id));
        if (!securityUtils.isAdmin()) {
            String connectedRole = securityUtils.getConnectedRole();
            final String currentUser = securityUtils.getConnectedUsername();
            boolean hasAccess;
            if ("ETUDIANT".equals(connectedRole)) {
                hasAccess = id.equals(securityUtils.getConnectedUserId());
            }
            else if ("FORMATEUR".equals(connectedRole)) {
                hasAccess = true;
            }
            else {
                hasAccess = true;
            }
            if (!hasAccess) {
                throw new org.springframework.security.access.AccessDeniedException(
                        "Vous ne pouvez supprimer que vos propres données");
            }
        }
        entity.getSessions().clear();
        repository.save(entity);
        if (entity.getEvaluations() != null && !entity.getEvaluations().isEmpty()) {
            evaluationRepository
                    .deleteAll(entity.getEvaluations());
        }
        if (entity.getCertificats() != null && !entity.getCertificats().isEmpty()) {
            certificatRepository
                    .deleteAll(entity.getCertificats());
        }
        if (entity.getInscriptions() != null && !entity.getInscriptions().isEmpty()) {
            inscriptionRepository
                    .deleteAll(entity.getInscriptions());
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<SessionResponseDTO> findSessionsByEtudiantId(Long id) {
        Etudiant entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ETUDIANT_NOT_FOUND + id));
        return entity.getSessions()
                .stream()
                .map(sessionMapper::toResponseDTO)
                .toList();
    }

    public void addSessionToEtudiant(Long etudiantId, Long sessionId) {
        Etudiant etudiant = repository.findById(etudiantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ETUDIANT_NOT_FOUND + etudiantId));
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Session not found with id: " + sessionId));
        boolean alreadyLinked = etudiant.getSessions().stream().anyMatch(item -> item.getId().equals(sessionId));
        if (!alreadyLinked) {
            etudiant.getSessions().add(session);
            repository.save(etudiant);
        }
    }

    public void removeSessionFromEtudiant(Long etudiantId, Long sessionId) {
        Etudiant etudiant = repository.findById(etudiantId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ETUDIANT_NOT_FOUND + etudiantId));
        etudiant.getSessions().removeIf(item -> item.getId().equals(sessionId));
        repository.save(etudiant);
    }

    @Transactional(readOnly = true)
    public List<EvaluationResponseDTO> findEvaluationsByEtudiantId(Long id) {
        Etudiant entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ETUDIANT_NOT_FOUND + id));
        return entity.getEvaluations()
                .stream()
                .map(evaluationMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CertificatResponseDTO> findCertificatsByEtudiantId(Long id) {
        Etudiant entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ETUDIANT_NOT_FOUND + id));
        return entity.getCertificats()
                .stream()
                .map(certificatMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<InscriptionResponseDTO> findInscriptionsByEtudiantId(Long id) {
        Etudiant entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ETUDIANT_NOT_FOUND + id));
        return entity.getInscriptions()
                .stream()
                .map(inscriptionMapper::toResponseDTO)
                .toList();
    }

    public Integer calculateDureeTotaleFormation(Long id) {
        Etudiant entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ETUDIANT_NOT_FOUND + id));

        List<Session> sessions = entity.getSessions();
        if (sessions == null || sessions.isEmpty()) return 0;

        return sessions.stream()
                .filter(s -> s.getFormation() != null)
                .filter(s -> s.getFormation().getDureeHeures() != null)
                .mapToInt(s -> s.getFormation().getDureeHeures())
                .sum();
    }
}
