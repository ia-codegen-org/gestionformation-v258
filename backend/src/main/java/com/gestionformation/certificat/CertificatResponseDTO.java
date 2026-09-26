package com.gestionformation.certificat;

import lombok.*;
import java.util.List;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

@Schema(description = "DTO de réponse pour Certificat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificatResponseDTO {

    @Schema(description = "Identifiant unique", example = "1")
    private Long id;

    @Schema(description = "Créé par", example = "formateur1")
    private String createdBy;

    @Schema(description = "titre de Certificat", example = "exemple")
    @NotBlank(message = "titre ne peut pas être vide")
    private String titre;

    @Schema(description = "dateObtention de Certificat", example = "2026-12-01")
    @Past(message = "dateObtention doit etre dans le passe")
    @NotNull(message = "dateObtention ne peut pas être null")
    private Date dateObtention;

    @Schema(description = "mention de Certificat", example = "exemple")
    @NotNull(message = "mention ne peut pas être null")
    private CertificatMention mention;

    @Schema(description = "ID de Etudiant", example = "1")
    private Long etudiantId;

}
