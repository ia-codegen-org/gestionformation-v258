package com.gestionformation.salle;

import lombok.*;
import java.util.List;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de réponse pour Salle")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalleResponseDTO {

    @Schema(description = "Identifiant unique", example = "1")
    private Long id;

    @Schema(description = "Créé par", example = "formateur1")
    private String createdBy;

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

    @Schema(description = "ID de Batiment", example = "1")
    private Long batimentId;

}
