package com.gestionformation.evaluation;

import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de mise à jour pour Evaluation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class EvaluationUpdateDTO {

    @Schema(description = "note de Evaluation", example = "1.0")
    @Min(value = 0, message = "note minimum 0")
    @Max(value = 20, message = "note maximum 20")
    @NotNull(message = "note ne peut pas être null")
    private Double note;

    @Schema(description = "commentaire de Evaluation", example = "exemple")
    @NotBlank(message = "commentaire ne peut pas être vide")
    private String commentaire;

    // Référence vers Etudiant
    private Long etudiantId;

    // Référence vers Session
    private Long sessionId;

}
