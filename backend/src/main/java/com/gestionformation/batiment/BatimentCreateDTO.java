package com.gestionformation.batiment;

import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de création pour Batiment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class BatimentCreateDTO {

    @Schema(description = "nom de Batiment", example = "exemple")
    @NotBlank(message = "nom ne peut pas être vide")
    private String nom;

    @Schema(description = "adresse de Batiment", example = "exemple")
    @NotBlank(message = "adresse ne peut pas être vide")
    private String adresse;

}
