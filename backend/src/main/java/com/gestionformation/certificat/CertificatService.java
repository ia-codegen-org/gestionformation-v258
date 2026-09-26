package com.gestionformation.certificat;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.security.SecurityUtils;
import com.gestionformation.etudiant.Etudiant;
import com.gestionformation.etudiant.EtudiantRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class CertificatService {

    private static final String CERTIFICAT_NOT_FOUND = "Certificat not found with id: ";

    private final CertificatRepository repository;
    private final CertificatMapper     mapper;
    private final SecurityUtils securityUtils;
    private final EtudiantRepository etudiantRepository;

    private java.util.stream.Stream<Certificat> accessibleStream() {
        if (securityUtils.isAdmin()) return repository.findAll().stream();
        final String currentUser = securityUtils.getConnectedUsername();
        if (currentUser == null) return java.util.stream.Stream.empty();
        return repository.findAll().stream()
                .filter(e ->
                    currentUser.equals(e.getCreatedBy())
                    || (e.getEtudiant() != null && currentUser.equals(e.getEtudiant().getUsername()))
                );
    }

    private void checkOwnership(Certificat entity, String message) {
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
    public List<CertificatResponseDTO> findAll() {
        return accessibleStream()
                .map(mapper::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public long count() {
        return accessibleStream().count();
    }

    @Transactional(readOnly = true)
    public java.util.Map<String, Long> countByMention() {
        return accessibleStream()
                .filter(e -> e.getMention() != null)
                .collect(java.util.stream.Collectors.groupingBy(
                        e -> e.getMention().toString(),
                        java.util.stream.Collectors.counting()));
    }

    @Transactional(readOnly = true)
    public CertificatResponseDTO findById(Long id) {
        Certificat entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        CERTIFICAT_NOT_FOUND + id));
        checkOwnership(entity, "Accès refusé");
        CertificatResponseDTO dto = mapper.toResponseDTO(entity);
        return dto;
    }

    public CertificatResponseDTO create(CertificatCreateDTO dto) {
        Certificat entity = mapper.toEntity(dto);
        if (!securityUtils.isAdmin()) {
            entity.setCreatedBy(securityUtils.getConnectedUsername());
        }
        return mapper.toResponseDTO(repository.save(entity));
    }

    public CertificatResponseDTO update(Long id, CertificatUpdateDTO dto) {
        Certificat entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        CERTIFICAT_NOT_FOUND + id));
        checkOwnership(entity, "Vous ne pouvez modifier que vos propres données");
        mapper.updateEntity(entity, dto);
        return mapper.toResponseDTO(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        Certificat entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        CERTIFICAT_NOT_FOUND + id));
        checkOwnership(entity, "Vous ne pouvez supprimer que vos propres données");
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<CertificatResponseDTO> findByEtudiantId(Long etudiantId) {
        return repository.findByEtudiantId(etudiantId)
                .stream()
                .map(mapper::toResponseDTO)
                .toList();
    }
}
