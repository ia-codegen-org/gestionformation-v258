package com.gestionformation.formateurexterne;

import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de mise à jour pour FormateurExterne")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class FormateurExterneUpdateDTO {

    @Schema(description = "Nom d'utilisateur", example = "formateurexterne.exemple")
    @NotBlank(message = "Le username ne peut pas être vide")
    private String username;

    @Schema(description = "Email", example = "formateurexterne@exemple.com")
    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "Format email invalide")
    private String email;

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
