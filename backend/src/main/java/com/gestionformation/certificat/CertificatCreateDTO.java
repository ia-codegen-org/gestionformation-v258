package com.gestionformation.certificat;

import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

@Schema(description = "DTO de création pour Certificat")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class CertificatCreateDTO {

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

    // Référence vers Etudiant
    private Long etudiantId;

}
