package com.gestionformation.facture;

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
@WebMvcTest(FactureController.class)
@AutoConfigureMockMvc(addFilters = false)
class FactureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private FactureService service;

    @MockitoBean
    private com.gestionformation.security.SecurityUtils securityUtils;

    @MockitoBean
    private com.gestionformation.security.JwtUtil jwtUtil;

    @MockitoBean
    private com.gestionformation.security.UserDetailsServiceImpl userDetailsService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAll_retourneLaListe() throws Exception {
        FactureResponseDTO dto = new FactureResponseDTO();
        dto.setId(1L);

        when(service.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/facture"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L));

        verify(service).findAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getById_quandEntiteExiste_retourne200() throws Exception {
        FactureResponseDTO dto = new FactureResponseDTO();
        dto.setId(1L);

        when(service.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/facture/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getById_quandEntiteAbsente_retourne404() throws Exception {
        when(service.findById(999L))
            .thenThrow(new com.gestionformation.exception.ResourceNotFoundException(
                "Facture not found"));

        mockMvc.perform(get("/api/facture/999"))
            .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void create_avecDonneesValides_retourne200() throws Exception {
        FactureCreateDTO createDto = new FactureCreateDTO();
        createDto.setNumero("test");
        createDto.setDate(new java.util.Date(System.currentTimeMillis() - 86400000L));
        createDto.setMontantTotal(1.0);
        createDto.setStatut(FactureStatut.EMISE);
        FactureResponseDTO responseDto = new FactureResponseDTO();
        responseDto.setId(1L);

        when(service.create(any(FactureCreateDTO.class)))
            .thenReturn(responseDto);

        mockMvc.perform(post("/api/facture")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_quandEntiteExiste_retourne204() throws Exception {
        doNothing().when(service).delete(1L);

        mockMvc.perform(delete("/api/facture/1"))
            .andExpect(status().isNoContent());

        verify(service).delete(1L);
    }

}
