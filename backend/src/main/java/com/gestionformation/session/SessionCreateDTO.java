package com.gestionformation.session;

import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

@Schema(description = "DTO de création pour Session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class SessionCreateDTO {

    @Schema(description = "dateDebut de Session", example = "2026-12-01")
    @Future(message = "dateDebut doit etre dans le futur")
    @NotNull(message = "dateDebut ne peut pas être null")
    private Date dateDebut;

    @Schema(description = "dateFin de Session", example = "2026-12-01")
    @Future(message = "dateFin doit etre dans le futur")
    @NotNull(message = "dateFin ne peut pas être null")
    private Date dateFin;

    // Référence vers Formation
    private Long formationId;

    // Référence vers Formateur
    private Long formateurId;

    // Référence vers Salle
    private Long salleId;

}
