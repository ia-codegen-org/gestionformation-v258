package com.gestionformation.formateurexterne;

import lombok.*;
import java.util.List;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de réponse pour FormateurExterne")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormateurExterneResponseDTO {

    @Schema(description = "Identifiant unique", example = "1")
    private Long id;

    @Schema(description = "Nom d'utilisateur", example = "formateurexterne.exemple")
    private String username;

    @Schema(description = "Email", example = "formateurexterne@exemple.com")
    private String email;

    @Schema(description = "Rôle", example = "FORMATEUR")
    private String role;

    @Schema(description = "Compte actif", example = "true")
    private boolean enabled;

    @Schema(description = "nom de FormateurExterne", example = "exemple")
    @NotBlank(message = "nom ne peut pas être vide")
    private String nom;

    @Schema(description = "prenom de FormateurExterne", example = "exemple")
    @NotBlank(message = "prenom ne peut pas être vide")
    private String prenom;

    @Schema(description = "anneesExperience de FormateurExterne", example = "1")
    @Min(value = 0, message = "anneesExperience doit etre positif")
    @NotNull(message = "anneesExperience ne peut pas être null")
    private Integer anneesExperience;

    @Schema(description = "tarifJournalier de FormateurExterne", example = "1.0")
    @Min(value = 0, message = "tarifJournalier doit etre positif")
    @NotNull(message = "tarifJournalier ne peut pas être null")
    private Double tarifJournalier;

}
