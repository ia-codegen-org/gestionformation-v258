package com.gestionformation.etudiant;

import lombok.*;
import java.util.List;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

@Schema(description = "DTO de réponse pour Etudiant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EtudiantResponseDTO {

    @Schema(description = "Identifiant unique", example = "1")
    private Long id;

    @Schema(description = "Nom d'utilisateur", example = "etudiant.exemple")
    private String username;

    @Schema(description = "Email", example = "etudiant@exemple.com")
    private String email;

    @Schema(description = "Rôle", example = "FORMATEUR")
    private String role;

    @Schema(description = "Compte actif", example = "true")
    private boolean enabled;

    @Schema(description = "nom de Etudiant", example = "exemple")
    @NotBlank(message = "nom ne peut pas être vide")
    private String nom;

    @Schema(description = "prenom de Etudiant", example = "exemple")
    @NotBlank(message = "prenom ne peut pas être vide")
    private String prenom;

    @Schema(description = "dateNaissance de Etudiant", example = "2026-12-01")
    @Past(message = "dateNaissance doit etre dans le passe")
    @NotNull(message = "dateNaissance ne peut pas être null")
    private Date dateNaissance;

    @Schema(description = "noteMoyenne")
    private Double noteMoyenne;

    @Schema(description = "dureeTotaleFormation")
    private Integer dureeTotaleFormation;

    @Schema(description = "nbCertificats")
    private Integer nbCertificats;

}
