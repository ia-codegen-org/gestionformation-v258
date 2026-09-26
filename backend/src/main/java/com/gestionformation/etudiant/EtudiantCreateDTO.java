package com.gestionformation.etudiant;

import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

@Schema(description = "DTO de création pour Etudiant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = false)
public class EtudiantCreateDTO {

    @Schema(description = "Nom d'utilisateur unique", example = "etudiant.exemple")
    @NotBlank(message = "Le username ne peut pas être vide")
    private String username;

    @Schema(description = "Email", example = "etudiant@exemple.com")
    @NotBlank(message = "L'email ne peut pas être vide")
    @Email(message = "Format email invalide")
    private String email;

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

}
