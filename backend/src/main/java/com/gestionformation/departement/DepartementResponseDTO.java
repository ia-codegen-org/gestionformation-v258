package com.gestionformation.departement;

import lombok.*;
import java.util.List;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de réponse pour Departement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartementResponseDTO {

    @Schema(description = "Identifiant unique", example = "1")
    private Long id;

    @Schema(description = "Créé par", example = "formateur1")
    private String createdBy;

    @Schema(description = "nom de Departement", example = "exemple")
    @NotBlank(message = "nom ne peut pas être vide")
    private String nom;

    @Schema(description = "budget de Departement", example = "1.0")
    @Min(value = 0, message = "budget doit etre positif")
    @NotNull(message = "budget ne peut pas être null")
    private Double budget;

    @Schema(description = "chiffreAffairesTotal")
    private Double chiffreAffairesTotal;

}
