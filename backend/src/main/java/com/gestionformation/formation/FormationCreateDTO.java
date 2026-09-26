package com.gestionformation.formation;

import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de création pour Formation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class FormationCreateDTO {

    @Schema(description = "titre de Formation", example = "exemple")
    @NotBlank(message = "titre ne peut pas être vide")
    private String titre;

    @Schema(description = "description de Formation", example = "exemple")
    @NotBlank(message = "description ne peut pas être vide")
    private String description;

    @Schema(description = "dureeHeures de Formation", example = "1")
    @Min(value = 0, message = "dureeHeures doit etre positif")
    @NotNull(message = "dureeHeures ne peut pas être null")
    private Integer dureeHeures;

    // Référence vers Categorie
    private Long categorieId;

}
