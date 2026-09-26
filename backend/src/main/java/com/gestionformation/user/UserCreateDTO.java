package com.gestionformation.user;

import jakarta.validation.constraints.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gestionformation.security.Role;

@Schema(description = "DTO de création pour User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class UserCreateDTO {

    @Schema(description = "Nom d'utilisateur unique", example = "johndoe")
    @NotBlank(message = "Le username ne peut pas être vide")
    private String username;

    @Schema(description = "Email unique", example = "john@example.com")
    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "Format email invalide")
    private String email;

    @Schema(description = "Mot de passe", example = "password123")
    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String password;

    @Schema(description = "Rôle de l'utilisateur", example = "AGENT")
    @NotNull(message = "Le rôle ne peut pas être null")
    private Role role;

}
