package com.gestionformation.evaluation;

import lombok.*;
import java.util.List;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de réponse pour Evaluation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluationResponseDTO {

    @Schema(description = "Identifiant unique", example = "1")
    private Long id;

    @Schema(description = "Créé par", example = "formateur1")
    private String createdBy;

    @Schema(description = "note de Evaluation", example = "1.0")
    @Min(value = 0, message = "note minimum 0")
    @Max(value = 20, message = "note maximum 20")
    @NotNull(message = "note ne peut pas être null")
    private Double note;

    @Schema(description = "commentaire de Evaluation", example = "exemple")
    @NotBlank(message = "commentaire ne peut pas être vide")
    private String commentaire;

    @Schema(description = "ID de Etudiant", example = "1")
    private Long etudiantId;

    @Schema(description = "ID de Session", example = "1")
    private Long sessionId;

}
