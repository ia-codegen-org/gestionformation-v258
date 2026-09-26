package com.gestionformation.formateurinterne;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.security.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.gestionformation.security.EmailService;

@Service
@Transactional
@RequiredArgsConstructor
public class FormateurInterneService {

    private static final String FORMATEURINTERNE_NOT_FOUND = "FormateurInterne not found with id: ";

    private static final java.security.SecureRandom RANDOM = new java.security.SecureRandom();

    private final FormateurInterneRepository repository;
    private final FormateurInterneMapper     mapper;
    private final SecurityUtils securityUtils;
    private final PasswordEncoder passwordEncoder;
    private final EmailService    emailService;

    @Transactional(readOnly = true)
    public List<FormateurInterneResponseDTO> findAll() {
        if (securityUtils.isAdmin()) {
            return repository.findAll().stream()
                    .map(mapper::toResponseDTO).toList();
        }

        String connectedRole = securityUtils.getConnectedRole();

        return repository.findAll().stream()
                .map(mapper::toResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    @Transactional(readOnly = true)
    public FormateurInterneResponseDTO findById(Long id) {
        FormateurInterne entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FORMATEURINTERNE_NOT_FOUND + id));
        FormateurInterneResponseDTO dto = mapper.toResponseDTO(entity);
        return dto;
    }

    public FormateurInterneResponseDTO create(FormateurInterneCreateDTO dto) {
        FormateurInterne entity = mapper.toEntity(dto);

        // ✅ Générer un mot de passe aléatoire sécurisé
        String generatedPassword = generateSecurePassword();
        entity.setPassword(passwordEncoder.encode(generatedPassword));
        entity.setMustChangePassword(true);
        entity.setEnabled(true);

        FormateurInterne saved = repository.save(entity);

        // ✅ Envoyer les identifiants par email
        emailService.sendCredentialsEmail(
                saved.getEmail(),
                saved.getUsername(),
                generatedPassword,
                "FormateurInterne"
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

    public FormateurInterneResponseDTO update(Long id, FormateurInterneUpdateDTO dto) {
        FormateurInterne entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FORMATEURINTERNE_NOT_FOUND + id));
        mapper.updateEntity(entity, dto);
        return mapper.toResponseDTO(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        FormateurInterne entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FORMATEURINTERNE_NOT_FOUND + id));
        repository.deleteById(id);
    }
}
