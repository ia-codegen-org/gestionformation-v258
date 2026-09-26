package com.gestionformation.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * Test unitaire généré automatiquement par IACodeGen.
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthServiceTest {

    @Mock
    private com.gestionformation.user.UserRepository userRepository;

    @Mock
    private com.gestionformation.user.UserMapper userMapper;

    @Mock
    private com.gestionformation.security.JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private com.gestionformation.security.EmailService emailService;

    @Mock
    private com.gestionformation.security.PasswordResetTokenRepository resetTokenRepository;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_avecNouvelUtilisateur_creeEtRetourneToken() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("password123");

        com.gestionformation.user.User user = new com.gestionformation.user.User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRole(com.gestionformation.security.Role.ADMIN);

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userMapper.toEntity(request)).thenReturn(user);
        when(jwtUtil.generateToken(user)).thenReturn("fake-token");

        AuthResponse response = authService.register(request);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("fake-token");
        verify(userRepository).save(user);
    }

    @Test
    void register_avecUsernameExistant_leveException() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existant");

        when(userRepository.existsByUsername("existant")).thenReturn(true);

        org.assertj.core.api.Assertions.assertThatThrownBy(
                () -> authService.register(request))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void login_avecIdentifiantsValides_retourneToken() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password123");

        com.gestionformation.user.User user = new com.gestionformation.user.User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRole(com.gestionformation.security.Role.ADMIN);

        when(userRepository.findByUsername("testuser"))
            .thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(user)).thenReturn("fake-token");

        AuthResponse response = authService.login(request);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("fake-token");
    }

    @Test
    void changePassword_avecAncienMotDePasseCorrect_metAJour() {
        com.gestionformation.user.User user = new com.gestionformation.user.User();
        user.setUsername("testuser");
        user.setPassword("encodedOld");

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("oldpass");
        request.setNewPassword("newpass");

        when(userRepository.findByUsername("testuser"))
            .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("oldpass", "encodedOld")).thenReturn(true);
        when(passwordEncoder.matches("newpass", "encodedOld")).thenReturn(false);
        when(passwordEncoder.encode("newpass")).thenReturn("encodedNew");

        authService.changePassword("testuser", request);

        verify(userRepository).save(user);
    }

    @Test
    void forgotPassword_avecEmailExistant_envoieEmail() {
        com.gestionformation.user.User user = new com.gestionformation.user.User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com"))
            .thenReturn(Optional.of(user));

        authService.forgotPassword(request, null);

        verify(emailService).sendPasswordResetEmail(eq("test@example.com"), any(), any());
    }

}
