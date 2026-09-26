package com.gestionformation.formateurinterne;

import lombok.*;
import java.util.List;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de réponse pour FormateurInterne")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormateurInterneResponseDTO {

    @Schema(description = "Identifiant unique", example = "1")
    private Long id;

    @Schema(description = "Nom d'utilisateur", example = "formateurinterne.exemple")
    private String username;

    @Schema(description = "Email", example = "formateurinterne@exemple.com")
    private String email;

    @Schema(description = "Rôle", example = "FORMATEUR")
    private String role;

    @Schema(description = "Compte actif", example = "true")
    private boolean enabled;

    @Schema(description = "nom de FormateurInterne", example = "exemple")
    @NotBlank(message = "nom ne peut pas être vide")
    private String nom;

    @Schema(description = "prenom de FormateurInterne", example = "exemple")
    @NotBlank(message = "prenom ne peut pas être vide")
    private String prenom;

    @Schema(description = "anneesExperience de FormateurInterne", example = "1")
    @Min(value = 0, message = "anneesExperience doit etre positif")
    @NotNull(message = "anneesExperience ne peut pas être null")
    private Integer anneesExperience;

    @Schema(description = "salaireMensuel de FormateurInterne", example = "1.0")
    @Min(value = 0, message = "salaireMensuel doit etre positif")
    @NotNull(message = "salaireMensuel ne peut pas être null")
    private Double salaireMensuel;

}
