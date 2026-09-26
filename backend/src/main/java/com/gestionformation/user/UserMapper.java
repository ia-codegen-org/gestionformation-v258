package com.gestionformation.user;

import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import com.gestionformation.security.Role;
import com.gestionformation.auth.RegisterRequest;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User toEntity(UserCreateDTO dto) {
        if (dto == null) return null;
        return User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .build();
    }

    public User toEntity(RegisterRequest request) {
        if (request == null) return null;
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
    }

    public UserResponseDTO toResponseDTO(User entity) {
        if (entity == null) return null;
        return UserResponseDTO.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .role(entity.getRole().name())
                .build();
    }

    public void updateEntity(User entity, UserUpdateDTO dto) {
        if (dto == null) return;
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
    }
}
