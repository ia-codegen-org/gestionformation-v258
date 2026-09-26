package com.gestionformation.departement;

import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de création pour Departement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class DepartementCreateDTO {

    @Schema(description = "nom de Departement", example = "exemple")
    @NotBlank(message = "nom ne peut pas être vide")
    private String nom;

    @Schema(description = "budget de Departement", example = "1.0")
    @Min(value = 0, message = "budget doit etre positif")
    @NotNull(message = "budget ne peut pas être null")
    private Double budget;

}
