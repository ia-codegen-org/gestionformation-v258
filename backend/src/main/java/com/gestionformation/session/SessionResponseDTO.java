package com.gestionformation.session;

import lombok.*;
import java.util.List;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

@Schema(description = "DTO de réponse pour Session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionResponseDTO {

    @Schema(description = "Identifiant unique", example = "1")
    private Long id;

    @Schema(description = "Créé par", example = "formateur1")
    private String createdBy;

    @Schema(description = "dateDebut de Session", example = "2026-12-01")
    @Future(message = "dateDebut doit etre dans le futur")
    @NotNull(message = "dateDebut ne peut pas être null")
    private Date dateDebut;

    @Schema(description = "dateFin de Session", example = "2026-12-01")
    @Future(message = "dateFin doit etre dans le futur")
    @NotNull(message = "dateFin ne peut pas être null")
    private Date dateFin;

    @Schema(description = "ID de Formation", example = "1")
    private Long formationId;

    @Schema(description = "ID de Formateur", example = "1")
    private Long formateurId;

    @Schema(description = "ID de Salle", example = "1")
    private Long salleId;

    @Schema(description = "Usernames des Etudiant assignés")
    private java.util.List<String> etudiantsUsernames;

    @Schema(description = "nbEtudiants")
    private Integer nbEtudiants;

    @Schema(description = "coutTotal")
    private Double coutTotal;

}
