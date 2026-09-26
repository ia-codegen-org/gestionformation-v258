package com.gestionformation.salle;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.exception.DuplicateResourceException;
import com.gestionformation.security.SecurityUtils;
import com.gestionformation.batiment.Batiment;
import com.gestionformation.batiment.BatimentRepository;
import com.gestionformation.session.Session;
import com.gestionformation.session.SessionRepository;
import com.gestionformation.session.SessionResponseDTO;
import com.gestionformation.session.SessionMapper;

@Service
@Transactional
@RequiredArgsConstructor
public class SalleService {

    private static final String SALLE_NOT_FOUND = "Salle not found with id: ";

    private final SalleRepository repository;
    private final SalleMapper     mapper;
    private final SecurityUtils securityUtils;
    private final BatimentRepository batimentRepository;
    private final SessionRepository sessionRepository;
    private final SessionMapper sessionMapper;

    private java.util.stream.Stream<Salle> accessibleStream() {
        if (securityUtils.isAdmin()) return repository.findAll().stream();
        final String currentUser = securityUtils.getConnectedUsername();
        if (currentUser == null) return java.util.stream.Stream.empty();
        return repository.findAll().stream()
                .filter(e ->
                    e.getCreatedBy() == null
                    || currentUser.equals(e.getCreatedBy())
                );
    }

    private void checkOwnership(Salle entity, String message) {
        if (securityUtils.isAdmin()) return;
        final String currentUser = securityUtils.getConnectedUsername();
        if (currentUser == null) return;
        if (!(entity.getCreatedBy() == null
                    || currentUser.equals(entity.getCreatedBy()))) {
            throw new org.springframework.security.access.AccessDeniedException(message);
        }
    }

    @Transactional(readOnly = true)
    public List<SalleResponseDTO> findAll() {
        return accessibleStream()
                .map(mapper::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public long count() {
        return accessibleStream().count();
    }

    @Transactional(readOnly = true)
    public SalleResponseDTO findById(Long id) {
        Salle entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        SALLE_NOT_FOUND + id));
        checkOwnership(entity, "Accès refusé");
        SalleResponseDTO dto = mapper.toResponseDTO(entity);
        return dto;
    }

    public SalleResponseDTO create(SalleCreateDTO dto) {
        Salle entity = mapper.toEntity(dto);
        if (repository.existsByNumero(dto.getNumero())) {
            throw new DuplicateResourceException("numero déjà utilisé");
        }
        if (!securityUtils.isAdmin()) {
            entity.setCreatedBy(securityUtils.getConnectedUsername());
        }
        return mapper.toResponseDTO(repository.save(entity));
    }

    public SalleResponseDTO update(Long id, SalleUpdateDTO dto) {
        Salle entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        SALLE_NOT_FOUND + id));
        checkOwnership(entity, "Vous ne pouvez modifier que vos propres données");
        if (repository.existsByNumeroAndIdNot(dto.getNumero(), id)) {
            throw new DuplicateResourceException("numero déjà utilisé");
        }
        mapper.updateEntity(entity, dto);
        return mapper.toResponseDTO(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        Salle entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        SALLE_NOT_FOUND + id));
        checkOwnership(entity, "Vous ne pouvez supprimer que vos propres données");
        if (entity.getSessions() != null && !entity.getSessions().isEmpty()) {
            sessionRepository
                    .deleteAll(entity.getSessions());
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<SalleResponseDTO> findByBatimentId(Long batimentId) {
        return repository.findByBatimentId(batimentId)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SessionResponseDTO> findSessionsBySalleId(Long id) {
        Salle entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        SALLE_NOT_FOUND + id));
        return entity.getSessions()
                .stream()
                .map(sessionMapper::toResponseDTO)
                .toList();
    }
}
