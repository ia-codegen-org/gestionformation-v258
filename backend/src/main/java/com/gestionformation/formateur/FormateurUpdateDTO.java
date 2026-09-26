package com.gestionformation.formateur;

import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de mise à jour pour Formateur")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class FormateurUpdateDTO {

    @Schema(description = "Nom d'utilisateur", example = "formateur.exemple")
    @NotBlank(message = "Le username ne peut pas être vide")
    private String username;

    @Schema(description = "Email", example = "formateur@exemple.com")
    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "Format email invalide")
    private String email;

    @Schema(description = "nom de Formateur", example = "exemple")
    @NotBlank(message = "nom ne peut pas être vide")
    private String nom;

    @Schema(description = "prenom de Formateur", example = "exemple")
    @NotBlank(message = "prenom ne peut pas être vide")
    private String prenom;

    @Schema(description = "anneesExperience de Formateur", example = "1")
    @Min(value = 0, message = "anneesExperience doit etre positif")
    @NotNull(message = "anneesExperience ne peut pas être null")
    private Integer anneesExperience;

    // Référence vers Departement
    private Long departementId;

}
