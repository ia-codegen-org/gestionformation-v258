package com.gestionformation.departement;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.security.SecurityUtils;
import com.gestionformation.formateur.Formateur;
import com.gestionformation.formateur.FormateurRepository;
import com.gestionformation.formateur.FormateurResponseDTO;
import com.gestionformation.formateur.FormateurMapper;
import com.gestionformation.session.Session;
import com.gestionformation.session.SessionRepository;
import com.gestionformation.inscription.Inscription;
import com.gestionformation.inscription.InscriptionRepository;
import com.gestionformation.paiement.Paiement;
import com.gestionformation.paiement.PaiementRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class DepartementService {

    private static final String DEPARTEMENT_NOT_FOUND = "Departement not found with id: ";

    private final DepartementRepository repository;
    private final DepartementMapper     mapper;
    private final SecurityUtils securityUtils;
    private final FormateurRepository formateurRepository;
    private final FormateurMapper formateurMapper;
    private final SessionRepository sessionRepository;
    private final InscriptionRepository inscriptionRepository;
    private final PaiementRepository paiementRepository;

    private java.util.stream.Stream<Departement> accessibleStream() {
        if (securityUtils.isAdmin()) return repository.findAll().stream();
        final String currentUser = securityUtils.getConnectedUsername();
        if (currentUser == null) return java.util.stream.Stream.empty();
        return repository.findAll().stream()
                .filter(e ->
                    e.getCreatedBy() == null
                    || currentUser.equals(e.getCreatedBy())
                );
    }

    private void checkOwnership(Departement entity, String message) {
        if (securityUtils.isAdmin()) return;
        final String currentUser = securityUtils.getConnectedUsername();
        if (currentUser == null) return;
        if (!(entity.getCreatedBy() == null
                    || currentUser.equals(entity.getCreatedBy()))) {
            throw new org.springframework.security.access.AccessDeniedException(message);
        }
    }

    @Transactional(readOnly = true)
    public List<DepartementResponseDTO> findAll() {
        return accessibleStream()
                .map(entity -> {
                    DepartementResponseDTO dto = mapper.toResponseDTO(entity);
                    dto.setChiffreAffairesTotal(calculateChiffreAffairesTotal(entity.getId()));
                    return dto;
                }).toList();
    }

    @Transactional(readOnly = true)
    public long count() {
        return accessibleStream().count();
    }

    @Transactional(readOnly = true)
    public DepartementResponseDTO findById(Long id) {
        Departement entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        DEPARTEMENT_NOT_FOUND + id));
        checkOwnership(entity, "Accès refusé");
        DepartementResponseDTO dto = mapper.toResponseDTO(entity);
        dto.setChiffreAffairesTotal(calculateChiffreAffairesTotal(id));
        return dto;
    }

    public DepartementResponseDTO create(DepartementCreateDTO dto) {
        Departement entity = mapper.toEntity(dto);
        if (!securityUtils.isAdmin()) {
            entity.setCreatedBy(securityUtils.getConnectedUsername());
        }
        return mapper.toResponseDTO(repository.save(entity));
    }

    public DepartementResponseDTO update(Long id, DepartementUpdateDTO dto) {
        Departement entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        DEPARTEMENT_NOT_FOUND + id));
        checkOwnership(entity, "Vous ne pouvez modifier que vos propres données");
        mapper.updateEntity(entity, dto);
        return mapper.toResponseDTO(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        Departement entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        DEPARTEMENT_NOT_FOUND + id));
        checkOwnership(entity, "Vous ne pouvez supprimer que vos propres données");
        if (entity.getFormateurs() != null && !entity.getFormateurs().isEmpty()) {
            formateurRepository
                    .deleteAll(entity.getFormateurs());
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<FormateurResponseDTO> findFormateursByDepartementId(Long id) {
        Departement entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        DEPARTEMENT_NOT_FOUND + id));
        return entity.getFormateurs()
                .stream()
                .map(formateurMapper::toResponseDTO)
                .toList();
    }

    public Double calculateChiffreAffairesTotal(Long id) {
        if (!repository.existsById(id))
            throw new ResourceNotFoundException(
                    DEPARTEMENT_NOT_FOUND + id);

        List<Formateur> step0 = formateurRepository.findByDepartementId(id);
        if (step0 == null || step0.isEmpty()) return 0.0;

        return step0.stream()
                .flatMap(e -> {
                    List<Session> sub = sessionRepository.findByFormateurId(e.getId());
                    return sub != null ? sub.stream() : java.util.stream.Stream.empty();
                })
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
