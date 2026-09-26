package com.gestionformation.formateurexterne;

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
public class FormateurExterneService {

    private static final String FORMATEUREXTERNE_NOT_FOUND = "FormateurExterne not found with id: ";

    private static final java.security.SecureRandom RANDOM = new java.security.SecureRandom();

    private final FormateurExterneRepository repository;
    private final FormateurExterneMapper     mapper;
    private final SecurityUtils securityUtils;
    private final PasswordEncoder passwordEncoder;
    private final EmailService    emailService;

    @Transactional(readOnly = true)
    public List<FormateurExterneResponseDTO> findAll() {
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
    public FormateurExterneResponseDTO findById(Long id) {
        FormateurExterne entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FORMATEUREXTERNE_NOT_FOUND + id));
        FormateurExterneResponseDTO dto = mapper.toResponseDTO(entity);
        return dto;
    }

    public FormateurExterneResponseDTO create(FormateurExterneCreateDTO dto) {
        FormateurExterne entity = mapper.toEntity(dto);

        // ✅ Générer un mot de passe aléatoire sécurisé
        String generatedPassword = generateSecurePassword();
        entity.setPassword(passwordEncoder.encode(generatedPassword));
        entity.setMustChangePassword(true);
        entity.setEnabled(true);

        FormateurExterne saved = repository.save(entity);

        // ✅ Envoyer les identifiants par email
        emailService.sendCredentialsEmail(
                saved.getEmail(),
                saved.getUsername(),
                generatedPassword,
                "FormateurExterne"
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

    public FormateurExterneResponseDTO update(Long id, FormateurExterneUpdateDTO dto) {
        FormateurExterne entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FORMATEUREXTERNE_NOT_FOUND + id));
        mapper.updateEntity(entity, dto);
        return mapper.toResponseDTO(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        FormateurExterne entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        FORMATEUREXTERNE_NOT_FOUND + id));
        repository.deleteById(id);
    }
}
