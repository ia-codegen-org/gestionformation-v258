package com.gestionformation.paiement;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.security.SecurityUtils;
import com.gestionformation.inscription.Inscription;
import com.gestionformation.inscription.InscriptionRepository;
import com.gestionformation.inscription.InscriptionResponseDTO;
import com.gestionformation.inscription.InscriptionMapper;
import com.gestionformation.facture.Facture;
import com.gestionformation.facture.FactureRepository;
import com.gestionformation.facture.FactureResponseDTO;
import com.gestionformation.facture.FactureMapper;

@Service
@Transactional
@RequiredArgsConstructor
public class PaiementService {

    private static final String PAIEMENT_NOT_FOUND = "Paiement not found with id: ";

    private final PaiementRepository repository;
    private final PaiementMapper     mapper;
    private final SecurityUtils securityUtils;
    private final InscriptionRepository inscriptionRepository;
    private final InscriptionMapper inscriptionMapper;
    private final FactureRepository factureRepository;
    private final FactureMapper factureMapper;

    private java.util.stream.Stream<Paiement> accessibleStream() {
        if (securityUtils.isAdmin()) return repository.findAll().stream();
        final String currentUser = securityUtils.getConnectedUsername();
        if (currentUser == null) return java.util.stream.Stream.empty();
        return repository.findAll().stream()
                .filter(e ->
                    e.getCreatedBy() == null
                    || currentUser.equals(e.getCreatedBy())
                );
    }

    private void checkOwnership(Paiement entity, String message) {
        if (securityUtils.isAdmin()) return;
        final String currentUser = securityUtils.getConnectedUsername();
        if (currentUser == null) return;
        if (!(entity.getCreatedBy() == null
                    || currentUser.equals(entity.getCreatedBy()))) {
            throw new org.springframework.security.access.AccessDeniedException(message);
        }
    }

    @Transactional(readOnly = true)
    public List<PaiementResponseDTO> findAll() {
        return accessibleStream()
                .map(mapper::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public long count() {
        return accessibleStream().count();
    }

    @Transactional(readOnly = true)
    public java.util.Map<String, Long> countByModePaiement() {
        return accessibleStream()
                .filter(e -> e.getModePaiement() != null)
                .collect(java.util.stream.Collectors.groupingBy(
                        e -> e.getModePaiement().toString(),
                        java.util.stream.Collectors.counting()));
    }

    @Transactional(readOnly = true)
    public PaiementResponseDTO findById(Long id) {
        Paiement entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        PAIEMENT_NOT_FOUND + id));
        checkOwnership(entity, "Accès refusé");
        PaiementResponseDTO dto = mapper.toResponseDTO(entity);
        return dto;
    }

    public PaiementResponseDTO create(PaiementCreateDTO dto) {
        Paiement entity = mapper.toEntity(dto);
        if (!securityUtils.isAdmin()) {
            entity.setCreatedBy(securityUtils.getConnectedUsername());
        }
        return mapper.toResponseDTO(repository.save(entity));
    }

    public PaiementResponseDTO update(Long id, PaiementUpdateDTO dto) {
        Paiement entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        PAIEMENT_NOT_FOUND + id));
        checkOwnership(entity, "Vous ne pouvez modifier que vos propres données");
        mapper.updateEntity(entity, dto);
        return mapper.toResponseDTO(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        Paiement entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        PAIEMENT_NOT_FOUND + id));
        checkOwnership(entity, "Vous ne pouvez supprimer que vos propres données");
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public InscriptionResponseDTO findInscriptionByPaiementId(Long id) {
        Paiement entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        PAIEMENT_NOT_FOUND + id));
        if (entity.getInscription() == null) {
            throw new ResourceNotFoundException(
                    "Inscription not found for paiement: " + id);
        }
        return inscriptionMapper.toResponseDTO(entity.getInscription());
    }

    @Transactional(readOnly = true)
    public FactureResponseDTO findFactureByPaiementId(Long id) {
        Paiement entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        PAIEMENT_NOT_FOUND + id));
        if (entity.getFacture() == null) {
            throw new ResourceNotFoundException(
                    "Facture not found for paiement: " + id);
        }
        return factureMapper.toResponseDTO(entity.getFacture());
    }
}
