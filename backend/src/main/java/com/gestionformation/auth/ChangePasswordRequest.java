package com.gestionformation.auth;

import jakarta.validation.constraints.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Schema(description = "DTO de changement de mot de passe")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class ChangePasswordRequest {

    @Schema(description = "Ancien mot de passe", example = "oldPass123")
    @NotBlank(message = "L'ancien mot de passe ne peut pas être vide")
    private String oldPassword;

    @Schema(description = "Nouveau mot de passe", example = "newPass456")
    @NotBlank(message = "Le nouveau mot de passe ne peut pas être vide")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    private String newPassword;

}
