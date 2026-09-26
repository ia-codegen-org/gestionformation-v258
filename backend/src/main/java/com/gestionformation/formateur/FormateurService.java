package com.gestionformation.formateur;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.security.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.gestionformation.security.EmailService;
import com.gestionformation.departement.Departement;
import com.gestionformation.departement.DepartementRepository;
import com.gestionformation.formation.Formation;
import com.gestionformation.formation.FormationRepository;
import com.gestionformation.formation.FormationResponseDTO;
import com.gestionformation.formation.FormationMapper;
import com.gestionformation.session.Session;
import com.gestionformation.session.SessionRepository;
import com.gestionformation.session.SessionResponseDTO;
import com.gestionformation.session.SessionMapper;
import com.gestionformation.inscription.Inscription;
import com.gestionformation.inscription.InscriptionRepository;
import com.gestionformation.paiement.Paiement;
import com.gestionformation.paiement.PaiementRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class FormateurService {

    private static final String FORMATEUR_NOT_FOUND = "Formateur not found with id: ";

    private static final java.security.SecureRandom RANDOM = new java.security.SecureRandom();

    private final FormateurRepository repository;
    private final FormateurMapper     mapper;
    private final SecurityUtils securityUtils;
    private final PasswordEncoder passwordEncoder;
    private final EmailService    emailService;
    private final DepartementRepository departementRepository;
    private final FormationRepository formationRepository;
    private final FormationMapper formationMapper;
    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;
    private final InscriptionRepository inscriptionRepository;
    private final PaiementRepository paiementRepository;

    @Transactional(readOnly = true)
    public List<FormateurResponseDTO> findAll() {
        if (securityUtils.isAdmin()) {
            return repository.findAll().stream()
                    .map(entity -> {
                        FormateurResponseDTO dto = mapper.toResponseDTO(entity);
                        dto.setChiffreAffairesTotal(calculateChiffreAffairesTotal(entity.getId()));
                        return dto;
                    }).toList();
        }

        String connectedRole = securityUtils.getConnectedRole();

        if ("FORMATEUR".equals(connectedRole)) {
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
    public FormateurResponseDTO findById(Long id) {
        Formateur entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FORMATEUR_NOT_FOUND + id));
        if (!securityUtils.isAdmin()) {
            String connectedRole = securityUtils.getConnectedRole();
            final String currentUser = securityUtils.getConnectedUsername();
            boolean hasAccess;
            if ("FORMATEUR".equals(connectedRole)) {
                hasAccess = id.equals(securityUtils.getConnectedUserId());
            }
            else {
                hasAccess = true;
            }
            if (!hasAccess) {
                throw new org.springframework.security.access.AccessDeniedException(
                        "Accès refusé");
            }
        }
        FormateurResponseDTO dto = mapper.toResponseDTO(entity);
        dto.setChiffreAffairesTotal(calculateChiffreAffairesTotal(id));
        return dto;
    }

    public FormateurResponseDTO create(FormateurCreateDTO dto) {
        Formateur entity = mapper.toEntity(dto);

        // ✅ Générer un mot de passe aléatoire sécurisé
        String generatedPassword = generateSecurePassword();
        entity.setPassword(passwordEncoder.encode(generatedPassword));
        entity.setMustChangePassword(true);
        entity.setEnabled(true);

        Formateur saved = repository.save(entity);

        // ✅ Envoyer les identifiants par email
        emailService.sendCredentialsEmail(
                saved.getEmail(),
                saved.getUsername(),
                generatedPassword,
                "Formateur"
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

    public FormateurResponseDTO update(Long id, FormateurUpdateDTO dto) {
        Formateur entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FORMATEUR_NOT_FOUND + id));
        if (!securityUtils.isAdmin()) {
            String connectedRole = securityUtils.getConnectedRole();
            final String currentUser = securityUtils.getConnectedUsername();
            boolean hasAccess;
            if ("FORMATEUR".equals(connectedRole)) {
                hasAccess = id.equals(securityUtils.getConnectedUserId());
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
        Formateur entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FORMATEUR_NOT_FOUND + id));
        if (!securityUtils.isAdmin()) {
            String connectedRole = securityUtils.getConnectedRole();
            final String currentUser = securityUtils.getConnectedUsername();
            boolean hasAccess;
            if ("FORMATEUR".equals(connectedRole)) {
                hasAccess = id.equals(securityUtils.getConnectedUserId());
            }
            else {
                hasAccess = true;
            }
            if (!hasAccess) {
                throw new org.springframework.security.access.AccessDeniedException(
                        "Vous ne pouvez supprimer que vos propres données");
            }
        }
        entity.getFormations().clear();
        repository.save(entity);
        if (entity.getSessions() != null && !entity.getSessions().isEmpty()) {
            sessionRepository
                    .deleteAll(entity.getSessions());
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<FormateurResponseDTO> findByDepartementId(Long departementId) {
        return repository.findByDepartementId(departementId)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FormationResponseDTO> findFormationsByFormateurId(Long id) {
        Formateur entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FORMATEUR_NOT_FOUND + id));
        return entity.getFormations()
                .stream()
                .map(formationMapper::toResponseDTO)
                .toList();
    }

    public void addFormationToFormateur(Long formateurId, Long formationId) {
        Formateur formateur = repository.findById(formateurId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FORMATEUR_NOT_FOUND + formateurId));
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Formation not found with id: " + formationId));
        boolean alreadyLinked = formateur.getFormations().stream().anyMatch(item -> item.getId().equals(formationId));
        if (!alreadyLinked) {
            formateur.getFormations().add(formation);
            repository.save(formateur);
        }
    }

    public void removeFormationFromFormateur(Long formateurId, Long formationId) {
        Formateur formateur = repository.findById(formateurId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FORMATEUR_NOT_FOUND + formateurId));
        formateur.getFormations().removeIf(item -> item.getId().equals(formationId));
        repository.save(formateur);
    }

    @Transactional(readOnly = true)
    public List<SessionResponseDTO> findSessionsByFormateurId(Long id) {
        Formateur entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FORMATEUR_NOT_FOUND + id));
        return entity.getSessions()
                .stream()
                .map(sessionMapper::toResponseDTO)
                .toList();
    }

    public Double calculateChiffreAffairesTotal(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException(
                    FORMATEUR_NOT_FOUND + id);

        List<Session> step0 = sessionRepository.findByFormateurId(id);
        if (step0 == null || step0.isEmpty()) return 0.0;

        return step0.stream()
                .flatMap(e -> {
                    List<Inscription> sub = inscriptionRepository.findBySessionId(e.getId());
                    return sub != null ? sub.stream() : java.util.stream.Stream.empty();
                })
                .flatMap(e -> {
                    List<Paiement> sub = paiementRepository.findByInscriptionId(e.getId());
                    return sub != null ? sub.stream() : java.util.stream.Stream.empty();
                })
                .filter(e -> e.getMontant() != null)
                .mapToDouble(e -> e.getMontant())
                .sum();
    }
}
