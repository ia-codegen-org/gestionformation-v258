package com.gestionformation.facture;

import lombok.*;
import java.util.List;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

@Schema(description = "DTO de réponse pour Facture")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FactureResponseDTO {

    @Schema(description = "Identifiant unique", example = "1")
    private Long id;

    @Schema(description = "Créé par", example = "formateur1")
    private String createdBy;

    @Schema(description = "numero de Facture", example = "exemple")
    @NotBlank(message = "numero ne peut pas être vide")
    private String numero;

    @Schema(description = "date de Facture", example = "2026-12-01")
    @Past(message = "date doit etre dans le passe")
    @NotNull(message = "date ne peut pas être null")
    private Date date;

    @Schema(description = "montantTotal de Facture", example = "1.0")
    @Min(value = 0, message = "montantTotal doit etre positif")
    @NotNull(message = "montantTotal ne peut pas être null")
    private Double montantTotal;

    @Schema(description = "statut de Facture", example = "exemple")
    @NotNull(message = "statut ne peut pas être null")
    private FactureStatut statut;

    @Schema(description = "ID de Paiement", example = "1")
    private Long paiementId;

}
