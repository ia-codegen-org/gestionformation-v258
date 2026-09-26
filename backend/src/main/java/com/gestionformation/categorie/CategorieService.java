package com.gestionformation.categorie;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.security.SecurityUtils;
import com.gestionformation.formation.Formation;
import com.gestionformation.formation.FormationRepository;
import com.gestionformation.formation.FormationResponseDTO;
import com.gestionformation.formation.FormationMapper;

@Service
@Transactional
@RequiredArgsConstructor
public class CategorieService {

    private static final String CATEGORIE_NOT_FOUND = "Categorie not found with id: ";

    private final CategorieRepository repository;
    private final CategorieMapper     mapper;
    private final SecurityUtils securityUtils;
    private final FormationRepository formationRepository;
    private final FormationMapper formationMapper;

    private java.util.stream.Stream<Categorie> accessibleStream() {
        if (securityUtils.isAdmin()) return repository.findAll().stream();
        final String currentUser = securityUtils.getConnectedUsername();
        if (currentUser == null) return java.util.stream.Stream.empty();
        return repository.findAll().stream()
                .filter(e ->
                    e.getCreatedBy() == null
                    || currentUser.equals(e.getCreatedBy())
                );
    }

    private void checkOwnership(Categorie entity, String message) {
        if (securityUtils.isAdmin()) return;
        final String currentUser = securityUtils.getConnectedUsername();
        if (currentUser == null) return;
        if (!(entity.getCreatedBy() == null
                    || currentUser.equals(entity.getCreatedBy()))) {
            throw new org.springframework.security.access.AccessDeniedException(message);
        }
    }

    @Transactional(readOnly = true)
    public List<CategorieResponseDTO> findAll() {
        return accessibleStream()
                .map(mapper::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public long count() {
        return accessibleStream().count();
    }

    @Transactional(readOnly = true)
    public CategorieResponseDTO findById(Long id) {
        Categorie entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        CATEGORIE_NOT_FOUND + id));
        checkOwnership(entity, "Accès refusé");
        CategorieResponseDTO dto = mapper.toResponseDTO(entity);
        return dto;
    }

    public CategorieResponseDTO create(CategorieCreateDTO dto) {
        Categorie entity = mapper.toEntity(dto);
        if (!securityUtils.isAdmin()) {
            entity.setCreatedBy(securityUtils.getConnectedUsername());
        }
        return mapper.toResponseDTO(repository.save(entity));
    }

    public CategorieResponseDTO update(Long id, CategorieUpdateDTO dto) {
        Categorie entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        CATEGORIE_NOT_FOUND + id));
        checkOwnership(entity, "Vous ne pouvez modifier que vos propres données");
        mapper.updateEntity(entity, dto);
        return mapper.toResponseDTO(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        Categorie entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        CATEGORIE_NOT_FOUND + id));
        checkOwnership(entity, "Vous ne pouvez supprimer que vos propres données");
        if (entity.getFormations() != null && !entity.getFormations().isEmpty()) {
            formationRepository
                    .deleteAll(entity.getFormations());
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<FormationResponseDTO> findFormationsByCategorieId(Long id) {
        Categorie entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        CATEGORIE_NOT_FOUND + id));
        return entity.getFormations()
                .stream()
                .map(formationMapper::toResponseDTO)
                .toList();
    }
}
