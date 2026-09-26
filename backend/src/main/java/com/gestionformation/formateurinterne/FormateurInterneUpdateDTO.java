package com.gestionformation.formateurinterne;

import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de mise à jour pour FormateurInterne")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class FormateurInterneUpdateDTO {

    @Schema(description = "Nom d'utilisateur", example = "formateurinterne.exemple")
    @NotBlank(message = "Le username ne peut pas être vide")
    private String username;

    @Schema(description = "Email", example = "formateurinterne@exemple.com")
    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "Format email invalide")
    private String email;

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
