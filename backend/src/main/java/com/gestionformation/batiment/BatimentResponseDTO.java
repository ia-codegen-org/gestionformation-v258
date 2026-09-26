package com.gestionformation.batiment;

import lombok.*;
import java.util.List;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de réponse pour Batiment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatimentResponseDTO {

    @Schema(description = "Identifiant unique", example = "1")
    private Long id;

    @Schema(description = "Créé par", example = "formateur1")
    private String createdBy;

    @Schema(description = "nom de Batiment", example = "exemple")
    @NotBlank(message = "nom ne peut pas être vide")
    private String nom;

    @Schema(description = "adresse de Batiment", example = "exemple")
    @NotBlank(message = "adresse ne peut pas être vide")
    private String adresse;

}
