package com.gestionformation.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test unitaire généré automatiquement par IACodeGen.
 */
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private com.gestionformation.user.UserService userService;

    @MockitoBean
    private com.gestionformation.user.UserRepository userRepository;

    @MockitoBean
    private com.gestionformation.security.SecurityUtils securityUtils;

    @MockitoBean
    private com.gestionformation.security.JwtUtil jwtUtil;

    @MockitoBean
    private com.gestionformation.security.UserDetailsServiceImpl userDetailsService;

    @Test
    void login_avecIdentifiantsValides_retourne200() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password123");

        AuthResponse response = new AuthResponse(1L, "fake-token", "testuser", "ADMIN", false);

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("fake-token"));
    }

    @Test
    void register_avecDonneesValides_retourne200() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("nouveluser");
        request.setEmail("nouvel@example.com");
        request.setPassword("password123");

        AuthResponse response = new AuthResponse(1L, "fake-token", "nouveluser", "ADMIN", false);

        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("fake-token"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void blockUser_retourne204() throws Exception {
        doNothing().when(userService).setEnabled(1L, false);

        mockMvc.perform(patch("/auth/users/1/block"))
            .andExpect(status().isNoContent());

        verify(userService).setEnabled(1L, false);
    }

    @Test
    void forgotPassword_retourne200() throws Exception {
        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("test@example.com");

        doNothing().when(authService)
            .forgotPassword(any(ForgotPasswordRequest.class), any());

        mockMvc.perform(post("/auth/forgot-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }

}
