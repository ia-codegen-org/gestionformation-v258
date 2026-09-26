package com.gestionformation.inscription;

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
import com.gestionformation.paiement.Paiement;
import com.gestionformation.paiement.PaiementRepository;
import com.gestionformation.paiement.PaiementResponseDTO;
import com.gestionformation.paiement.PaiementMapper;

@Service
@Transactional
@RequiredArgsConstructor
public class InscriptionService {

    private static final String INSCRIPTION_NOT_FOUND = "Inscription not found with id: ";

    private final InscriptionRepository repository;
    private final InscriptionMapper     mapper;
    private final SecurityUtils securityUtils;
    private final EtudiantRepository etudiantRepository;
    private final SessionRepository sessionRepository;
    private final PaiementRepository paiementRepository;
    private final PaiementMapper paiementMapper;

    private java.util.stream.Stream<Inscription> accessibleStream() {
        if (securityUtils.isAdmin()) return repository.findAll().stream();
        final String currentUser = securityUtils.getConnectedUsername();
        if (currentUser == null) return java.util.stream.Stream.empty();
        return repository.findAll().stream()
                .filter(e ->
                    currentUser.equals(e.getCreatedBy())
                    || (e.getEtudiant() != null && currentUser.equals(e.getEtudiant().getUsername()))
                );
    }

    private void checkOwnership(Inscription entity, String message) {
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
    public List<InscriptionResponseDTO> findAll() {
        return accessibleStream()
                .map(mapper::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public long count() {
        return accessibleStream().count();
    }

    @Transactional(readOnly = true)
    public java.util.Map<String, Long> countByStatut() {
        return accessibleStream()
                .filter(e -> e.getStatut() != null)
                .collect(java.util.stream.Collectors.groupingBy(
                        e -> e.getStatut().toString(),
                        java.util.stream.Collectors.counting()));
    }

    @Transactional(readOnly = true)
    public InscriptionResponseDTO findById(Long id) {
        Inscription entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        INSCRIPTION_NOT_FOUND + id));
        checkOwnership(entity, "Accès refusé");
        InscriptionResponseDTO dto = mapper.toResponseDTO(entity);
        return dto;
    }

    public InscriptionResponseDTO create(InscriptionCreateDTO dto) {
        Inscription entity = mapper.toEntity(dto);
        if (!securityUtils.isAdmin()) {
            entity.setCreatedBy(securityUtils.getConnectedUsername());
        }
        return mapper.toResponseDTO(repository.save(entity));
    }

    public InscriptionResponseDTO update(Long id, InscriptionUpdateDTO dto) {
        Inscription entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        INSCRIPTION_NOT_FOUND + id));
        checkOwnership(entity, "Vous ne pouvez modifier que vos propres données");
        mapper.updateEntity(entity, dto);
        return mapper.toResponseDTO(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        Inscription entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        INSCRIPTION_NOT_FOUND + id));
        checkOwnership(entity, "Vous ne pouvez supprimer que vos propres données");
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<InscriptionResponseDTO> findByEtudiantId(Long etudiantId) {
        return repository.findByEtudiantId(etudiantId)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<InscriptionResponseDTO> findBySessionId(Long sessionId) {
        return repository.findBySessionId(sessionId)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public PaiementResponseDTO findPaiementByInscriptionId(Long id) {
        Inscription entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        INSCRIPTION_NOT_FOUND + id));
        if (entity.getPaiement() == null) {
            throw new ResourceNotFoundException(
                    "Paiement not found for inscription: " + id);
        }
        return paiementMapper.toResponseDTO(entity.getPaiement());
    }
}
