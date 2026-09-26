package com.gestionformation.facture;

import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

@Schema(description = "DTO de mise à jour pour Facture")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class FactureUpdateDTO {

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

    // Référence vers Paiement
    private Long paiementId;

}
