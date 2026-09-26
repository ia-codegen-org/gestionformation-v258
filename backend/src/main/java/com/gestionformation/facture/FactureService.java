package com.gestionformation.facture;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.exception.DuplicateResourceException;
import com.gestionformation.security.SecurityUtils;
import com.gestionformation.paiement.Paiement;
import com.gestionformation.paiement.PaiementRepository;
import com.gestionformation.paiement.PaiementResponseDTO;
import com.gestionformation.paiement.PaiementMapper;

@Service
@Transactional
@RequiredArgsConstructor
public class FactureService {

    private static final String FACTURE_NOT_FOUND = "Facture not found with id: ";

    private final FactureRepository repository;
    private final FactureMapper     mapper;
    private final SecurityUtils securityUtils;
    private final PaiementRepository paiementRepository;
    private final PaiementMapper paiementMapper;

    private java.util.stream.Stream<Facture> accessibleStream() {
        if (securityUtils.isAdmin()) return repository.findAll().stream();
        final String currentUser = securityUtils.getConnectedUsername();
        if (currentUser == null) return java.util.stream.Stream.empty();
        return repository.findAll().stream()
                .filter(e ->
                    e.getCreatedBy() == null
                    || currentUser.equals(e.getCreatedBy())
                );
    }

    private void checkOwnership(Facture entity, String message) {
        if (securityUtils.isAdmin()) return;
        final String currentUser = securityUtils.getConnectedUsername();
        if (currentUser == null) return;
        if (!(entity.getCreatedBy() == null
                    || currentUser.equals(entity.getCreatedBy()))) {
            throw new org.springframework.security.access.AccessDeniedException(message);
        }
    }

    @Transactional(readOnly = true)
    public List<FactureResponseDTO> findAll() {
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
    public FactureResponseDTO findById(Long id) {
        Facture entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FACTURE_NOT_FOUND + id));
        checkOwnership(entity, "Accès refusé");
        FactureResponseDTO dto = mapper.toResponseDTO(entity);
        return dto;
    }

    public FactureResponseDTO create(FactureCreateDTO dto) {
        Facture entity = mapper.toEntity(dto);
        if (repository.existsByNumero(dto.getNumero())) {
            throw new DuplicateResourceException("numero déjà utilisé");
        }
        if (!securityUtils.isAdmin()) {
            entity.setCreatedBy(securityUtils.getConnectedUsername());
        }
        return mapper.toResponseDTO(repository.save(entity));
    }

    public FactureResponseDTO update(Long id, FactureUpdateDTO dto) {
        Facture entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FACTURE_NOT_FOUND + id));
        checkOwnership(entity, "Vous ne pouvez modifier que vos propres données");
        if (repository.existsByNumeroAndIdNot(dto.getNumero(), id)) {
            throw new DuplicateResourceException("numero déjà utilisé");
        }
        mapper.updateEntity(entity, dto);
        return mapper.toResponseDTO(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        Facture entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FACTURE_NOT_FOUND + id));
        checkOwnership(entity, "Vous ne pouvez supprimer que vos propres données");
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public PaiementResponseDTO findPaiementByFactureId(Long id) {
        Facture entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FACTURE_NOT_FOUND + id));
        if (entity.getPaiement() == null) {
            throw new ResourceNotFoundException(
                    "Paiement not found for facture: " + id);
        }
        return paiementMapper.toResponseDTO(entity.getPaiement());
    }
}
