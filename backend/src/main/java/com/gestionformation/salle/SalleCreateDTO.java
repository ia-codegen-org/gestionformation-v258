package com.gestionformation.salle;

import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de création pour Salle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class SalleCreateDTO {

    @Schema(description = "numero de Salle", example = "exemple")
    @NotBlank(message = "numero ne peut pas être vide")
    private String numero;

    @Schema(description = "capaciteMax de Salle", example = "1")
    @Min(value = 0, message = "capaciteMax doit etre positif")
    @NotNull(message = "capaciteMax ne peut pas être null")
    private Integer capaciteMax;

    @Schema(description = "equipement de Salle", example = "exemple")
    @NotBlank(message = "equipement ne peut pas être vide")
    private String equipement;

    // Référence vers Batiment
    private Long batimentId;

}
