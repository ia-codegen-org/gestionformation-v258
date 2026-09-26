package com.gestionformation.user;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.gestionformation.security.Role;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            String rawPassword = generateSecurePassword();
            User admin = User.builder()
                    .username("admin")
                    .email("admin@app.com")
                    .password(passwordEncoder.encode(rawPassword))
                    .role(Role.ADMIN)
                    .mustChangePassword(true)
                    .build();
            userRepository.save(admin);
            log.warn("╔══════════════════════════════════════════╗");
            log.warn("║       ADMIN CRÉÉ — NOTEZ CE MOT DE PASSE ║");
            log.warn("║  username : admin                        ║");
            log.warn("║  password : {}                           ║", rawPassword);
            log.warn("║  ⚠️  Changez-le à la première connexion  ║");
            log.warn("╚══════════════════════════════════════════╝");
        } else {
            log.info("ℹ️ Admin déjà existant — aucune création");
        }
    }

    private String generateSecurePassword() {
        byte[] bytes = new byte[16];
        RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(bytes).substring(0, 16);
    }
}
