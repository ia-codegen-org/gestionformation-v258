package com.gestionformation.auth;

import jakarta.validation.constraints.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Schema(description = "DTO de connexion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class LoginRequest {

    @Schema(description = "Nom d'utilisateur", example = "johndoe")
    @NotBlank(message = "Le username ne peut pas être vide")
    private String username;

    @Schema(description = "Mot de passe", example = "password123")
    @NotBlank(message = "Le mot de passe ne peut pas être vide")
    private String password;

}
