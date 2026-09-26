package com.gestionformation.inscription;

import lombok.*;
import java.util.List;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

@Schema(description = "DTO de réponse pour Inscription")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscriptionResponseDTO {

    @Schema(description = "Identifiant unique", example = "1")
    private Long id;

    @Schema(description = "Créé par", example = "formateur1")
    private String createdBy;

    @Schema(description = "statut de Inscription", example = "exemple")
    @NotNull(message = "statut ne peut pas être null")
    private InscriptionStatut statut;

    @Schema(description = "dateInscription de Inscription", example = "2026-12-01")
    @Past(message = "dateInscription doit etre dans le passe")
    @NotNull(message = "dateInscription ne peut pas être null")
    private Date dateInscription;

    @Schema(description = "ID de Etudiant", example = "1")
    private Long etudiantId;

    @Schema(description = "ID de Session", example = "1")
    private Long sessionId;

    @Schema(description = "ID de Paiement", example = "1")
    private Long paiementId;

}
