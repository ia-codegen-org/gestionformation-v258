package com.gestionformation.categorie;

import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de création pour Categorie")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class CategorieCreateDTO {

    @Schema(description = "nom de Categorie", example = "exemple")
    @NotBlank(message = "nom ne peut pas être vide")
    private String nom;

    @Schema(description = "description de Categorie", example = "exemple")
    @NotBlank(message = "description ne peut pas être vide")
    private String description;

}
