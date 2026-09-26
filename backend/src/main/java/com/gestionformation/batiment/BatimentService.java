package com.gestionformation.batiment;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.salle.Salle;
import com.gestionformation.salle.SalleRepository;
import com.gestionformation.salle.SalleResponseDTO;
import com.gestionformation.salle.SalleMapper;

@Service
@Transactional
@RequiredArgsConstructor
public class BatimentService {

    private static final String BATIMENT_NOT_FOUND = "Batiment not found with id: ";

    private final BatimentRepository repository;
    private final BatimentMapper     mapper;
    private final SalleRepository salleRepository;
    private final SalleMapper salleMapper;

    @Transactional(readOnly = true)
    public List<BatimentResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    @Transactional(readOnly = true)
    public BatimentResponseDTO findById(Long id) {
        Batiment entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        BATIMENT_NOT_FOUND + id));
        BatimentResponseDTO dto = mapper.toResponseDTO(entity);
        return dto;
    }

    public BatimentResponseDTO create(BatimentCreateDTO dto) {
        return mapper.toResponseDTO(repository.save(mapper.toEntity(dto)));
    }

    public BatimentResponseDTO update(Long id, BatimentUpdateDTO dto) {
        Batiment entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        BATIMENT_NOT_FOUND + id));
        mapper.updateEntity(entity, dto);
        return mapper.toResponseDTO(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        Batiment entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        BATIMENT_NOT_FOUND + id));
        if (entity.getSalles() != null && !entity.getSalles().isEmpty()) {
            salleRepository
                    .deleteAll(entity.getSalles());
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<SalleResponseDTO> findSallesByBatimentId(Long id) {
        Batiment entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        BATIMENT_NOT_FOUND + id));
        return entity.getSalles()
                .stream()
                .map(salleMapper::toResponseDTO)
                .toList();
    }
}
