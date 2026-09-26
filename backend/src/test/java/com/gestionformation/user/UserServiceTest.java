package com.gestionformation.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Test unitaire généré automatiquement par IACodeGen.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService service;

    @Test
    void findAll_retourneLaListe() {
        User entity = new User();
        entity.setId(1L);
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(1L);

        when(repository.findAll()).thenReturn(List.of(entity));
        when(mapper.toResponseDTO(entity)).thenReturn(dto);

        List<UserResponseDTO> result = service.findAll();

        assertThat(result).hasSize(1);
    }

    @Test
    void findById_quandUtilisateurExiste_retourneLeDTO() {
        User entity = new User();
        entity.setId(1L);
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(mapper.toResponseDTO(entity)).thenReturn(dto);

        UserResponseDTO result = service.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    void findById_quandUtilisateurAbsent_leveResourceNotFoundException() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(999L))
            .isInstanceOf(com.gestionformation.exception.ResourceNotFoundException.class);
    }

    @Test
    void setEnabled_modifieEtSauvegarde() {
        User entity = new User();
        entity.setId(1L);
        entity.setEnabled(false);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.setEnabled(1L, true);

        verify(repository).save(entity);
        assertThat(entity.isEnabled()).isTrue();
    }

}
