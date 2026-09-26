package com.gestionformation.certificat;

import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.etudiant.Etudiant;
import com.gestionformation.etudiant.EtudiantRepository;

@Component
@RequiredArgsConstructor
public class CertificatMapper {

    private final EtudiantRepository etudiantRepository;


public CertificatResponseDTO toResponseDTO(Certificat entity) {
    if (entity == null) return null;
    return CertificatResponseDTO.builder()
            .id(entity.getId())
                .createdBy(entity.getCreatedBy())
                .titre(entity.getTitre())
                .dateObtention(entity.getDateObtention())
                .mention(entity.getMention())
                .etudiantId(entity.getEtudiant() != null ? entity.getEtudiant().getId() : null)
                .build();
}


public Certificat toEntity(CertificatCreateDTO dto) {
    if (dto == null) return null;
    return Certificat.builder()
                .titre(dto.getTitre())
                .dateObtention(dto.getDateObtention())
                .mention(dto.getMention())
        .etudiant(dto.getEtudiantId() != null
                ? etudiantRepository.findById(dto.getEtudiantId())
                        .orElseThrow(() -> new ResourceNotFoundException(
                                "Etudiant not found with id: " + dto.getEtudiantId()))
                : null)
                .build();
}


public void updateEntity(Certificat entity, CertificatUpdateDTO dto) {
    if (dto == null) return;
        entity.setTitre(dto.getTitre());
        entity.setDateObtention(dto.getDateObtention());
        entity.setMention(dto.getMention());
if (dto.getEtudiantId() != null) {
    entity.setEtudiant(etudiantRepository.findById(dto.getEtudiantId())
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Etudiant not found with id: " + dto.getEtudiantId())));
}
    }

}
