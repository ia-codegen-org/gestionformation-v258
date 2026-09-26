package com.gestionformation.formation;

import lombok.*;
import java.util.List;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de réponse pour Formation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormationResponseDTO {

    @Schema(description = "Identifiant unique", example = "1")
    private Long id;

    @Schema(description = "Créé par", example = "formateur1")
    private String createdBy;

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

    @Schema(description = "ID de Categorie", example = "1")
    private Long categorieId;

    @Schema(description = "Usernames des Formateur assignés")
    private java.util.List<String> formateursUsernames;

    @Schema(description = "tauxReussite")
    private Double tauxReussite;

}
