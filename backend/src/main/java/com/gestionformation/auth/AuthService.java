package com.gestionformation.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import com.gestionformation.user.User;
import com.gestionformation.user.UserRepository;
import com.gestionformation.user.UserMapper;
import com.gestionformation.security.JwtUtil;
import com.gestionformation.security.Role;
import com.gestionformation.security.EmailService;
import com.gestionformation.security.PasswordResetToken;
import com.gestionformation.security.PasswordResetTokenRepository;
import com.gestionformation.exception.ResourceNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository        userRepository;
    private final UserMapper            userMapper;
    private final JwtUtil               jwtUtil;
    private final PasswordEncoder       passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService                 emailService;
    private final PasswordResetTokenRepository resetTokenRepository;

    @org.springframework.beans.factory.annotation.Value("${app.frontend-url}")
    private String frontendUrl;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username déjà utilisé : " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email déjà utilisé : " + request.getEmail());
        }
        User user = userMapper.toEntity(request);
        userRepository.save(user);
        String token = jwtUtil.generateToken(
                java.util.Map.of("role", user.getRole().name()), user);
        return new AuthResponse(user.getId(),token, user.getUsername(), user.getRole().name(), false);
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword()));
        } catch (org.springframework.security.authentication.DisabledException e) {
            throw new com.gestionformation.exception.AccountBlockedException(
                    "Compte bloqué : " + request.getUsername());
        }
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found : " + request.getUsername()));
        String token = jwtUtil.generateToken(
                java.util.Map.of("role", user.getRole().name()), user);
        return new AuthResponse(user.getId(), token, user.getUsername(),
                user.getRole().name(), user.isMustChangePassword());
    }

    public void changePassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found : " + username));
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Ancien mot de passe incorrect");
        }
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new IllegalArgumentException(
                    "Le nouveau mot de passe doit être différent de l'ancien");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setMustChangePassword(false);
        userRepository.save(user);
    }

    public void forgotPassword(ForgotPasswordRequest request, String origin) {
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            resetTokenRepository.deleteByUsername(user.getUsername());

            String token = java.util.UUID.randomUUID().toString();
            PasswordResetToken resetToken = PasswordResetToken.builder()
                    .token(token)
                    .username(user.getUsername())
                    .expiryDate(java.time.LocalDateTime.now().plusMinutes(30))
                    .used(false)
                    .build();
            resetTokenRepository.save(resetToken);

            String effectiveUrl = (origin != null && !origin.isBlank())
                    ? origin : frontendUrl;
            emailService.sendPasswordResetEmail(user.getEmail(), token, effectiveUrl);
        });
    }

    public void resetPassword(ResetPasswordRequest request) {
        PasswordResetToken resetToken = resetTokenRepository
                .findByToken(request.getToken())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Lien de réinitialisation invalide"));

        if (resetToken.isUsed()) {
            throw new IllegalArgumentException("Ce lien a déjà été utilisé");
        }
        if (resetToken.isExpired()) {
            throw new IllegalArgumentException("Ce lien a expiré");
        }

        User user = userRepository.findByUsername(resetToken.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setMustChangePassword(false);
        userRepository.save(user);

        resetToken.setUsed(true);
        resetTokenRepository.save(resetToken);
    }
}
