package com.gestionformation.paiement;

import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

@Schema(description = "DTO de création pour Paiement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class PaiementCreateDTO {

    @Schema(description = "montant de Paiement", example = "1.0")
    @Min(value = 0, message = "montant doit etre positif")
    @NotNull(message = "montant ne peut pas être null")
    private Double montant;

    @Schema(description = "date de Paiement", example = "2026-12-01")
    @Past(message = "date doit etre dans le passe")
    @NotNull(message = "date ne peut pas être null")
    private Date date;

    @Schema(description = "modePaiement de Paiement", example = "exemple")
    @NotNull(message = "modePaiement ne peut pas être null")
    private PaiementModePaiement modePaiement;

}
