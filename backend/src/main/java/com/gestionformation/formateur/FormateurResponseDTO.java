package com.gestionformation.formateur;

import lombok.*;
import java.util.List;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de réponse pour Formateur")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormateurResponseDTO {

    @Schema(description = "Identifiant unique", example = "1")
    private Long id;

    @Schema(description = "Nom d'utilisateur", example = "formateur.exemple")
    private String username;

    @Schema(description = "Email", example = "formateur@exemple.com")
    private String email;

    @Schema(description = "Rôle", example = "FORMATEUR")
    private String role;

    @Schema(description = "Compte actif", example = "true")
    private boolean enabled;

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

    @Schema(description = "ID de Departement", example = "1")
    private Long departementId;

    @Schema(description = "nbSessions")
    private Integer nbSessions;

    @Schema(description = "chiffreAffairesTotal")
    private Double chiffreAffairesTotal;

}
