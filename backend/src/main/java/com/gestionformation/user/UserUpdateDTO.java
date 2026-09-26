package com.gestionformation.user;

import jakarta.validation.constraints.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Schema(description = "DTO de mise à jour pour User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class UserUpdateDTO {

    @Schema(description = "Nom d'utilisateur unique", example = "johndoe")
    @NotBlank(message = "Le username ne peut pas être vide")
    private String username;

    @Schema(description = "Email unique", example = "john@example.com")
    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "Format email invalide")
    private String email;

}
