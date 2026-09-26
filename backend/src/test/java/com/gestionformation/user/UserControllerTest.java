package com.gestionformation.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test unitaire généré automatiquement par IACodeGen.
 */
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService service;

    @MockitoBean
    private com.gestionformation.security.SecurityUtils securityUtils;

    @MockitoBean
    private com.gestionformation.security.JwtUtil jwtUtil;

    @MockitoBean
    private com.gestionformation.security.UserDetailsServiceImpl userDetailsService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void findAll_retourneLaListe() throws Exception {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(1L);

        when(service.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void findById_retourne200() throws Exception {
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(1L);

        when(service.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L));
    }

}
