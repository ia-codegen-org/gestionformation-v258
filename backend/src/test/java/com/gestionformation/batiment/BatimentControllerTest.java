package com.gestionformation.batiment;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test unitaire du Controller généré automatiquement par IACodeGen.
 */
@WebMvcTest(BatimentController.class)
@AutoConfigureMockMvc(addFilters = false)
class BatimentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private BatimentService service;

    @MockitoBean
    private com.gestionformation.security.SecurityUtils securityUtils;

    @MockitoBean
    private com.gestionformation.security.JwtUtil jwtUtil;

    @MockitoBean
    private com.gestionformation.security.UserDetailsServiceImpl userDetailsService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAll_retourneLaListe() throws Exception {
        BatimentResponseDTO dto = new BatimentResponseDTO();
        dto.setId(1L);

        when(service.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/batiment"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L));

        verify(service).findAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getById_quandEntiteExiste_retourne200() throws Exception {
        BatimentResponseDTO dto = new BatimentResponseDTO();
        dto.setId(1L);

        when(service.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/batiment/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getById_quandEntiteAbsente_retourne404() throws Exception {
        when(service.findById(999L))
            .thenThrow(new com.gestionformation.exception.ResourceNotFoundException(
                "Batiment not found"));

        mockMvc.perform(get("/api/batiment/999"))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create_avecDonneesValides_retourne200() throws Exception {
        BatimentCreateDTO createDto = new BatimentCreateDTO();
        createDto.setNom("test");
        createDto.setAdresse("test");
        BatimentResponseDTO responseDto = new BatimentResponseDTO();
        responseDto.setId(1L);

        when(service.create(any(BatimentCreateDTO.class)))
            .thenReturn(responseDto);

        mockMvc.perform(post("/api/batiment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_quandEntiteExiste_retourne204() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/batiment/1"))
            .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }

}
