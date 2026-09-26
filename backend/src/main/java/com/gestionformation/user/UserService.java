package com.gestionformation.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import java.util.List;
import com.gestionformation.exception.ResourceNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final UserMapper     mapper;

    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO).toList();
    }

    public void setEnabled(Long id, boolean enabled) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));
        user.setEnabled(enabled);
        repository.save(user);
    }
    @Transactional(readOnly = true)
    public UserResponseDTO findById(Long id) {
        User entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));
        return mapper.toResponseDTO(entity);
    }
}
