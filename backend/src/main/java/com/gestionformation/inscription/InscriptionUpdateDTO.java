package com.gestionformation.inscription;

import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

@Schema(description = "DTO de mise à jour pour Inscription")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class InscriptionUpdateDTO {

    @Schema(description = "statut de Inscription", example = "exemple")
    @NotNull(message = "statut ne peut pas être null")
    private InscriptionStatut statut;

    @Schema(description = "dateInscription de Inscription", example = "2026-12-01")
    @Past(message = "dateInscription doit etre dans le passe")
    @NotNull(message = "dateInscription ne peut pas être null")
    private Date dateInscription;

    // Référence vers Etudiant
    private Long etudiantId;

    // Référence vers Session
    private Long sessionId;

    // Référence vers Paiement
    private Long paiementId;

}
