package com.gestionformation.formateur;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import com.gestionformation.security.SecurityUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.gestionformation.departement.DepartementResponseDTO;
import com.gestionformation.formation.FormationResponseDTO;
import com.gestionformation.session.SessionResponseDTO;

@Tag(name = "Formateur", description = "API de gestion des Formateur")
@RestController
@RequestMapping("/api/formateur")
@RequiredArgsConstructor
public class FormateurController {

    private final FormateurService service;

    private final SecurityUtils securityUtils;

    @Operation(
        summary = "Lister tous les Formateur",
        description = "Retourne la liste complète des Formateur"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Formateur', 'GET')")
    @GetMapping
    public ResponseEntity<List<FormateurResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
        summary = "Compter les Formateur",
        description = "Retourne le nombre total de Formateur"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nombre retourné avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Formateur', 'GET')")
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @Operation(
        summary = "Trouver un Formateur par ID",
        description = "Retourne un Formateur selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Formateur trouvé"),
        @ApiResponse(responseCode = "404", description = "Formateur non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Formateur', 'GET')")
    @GetMapping("/{id}")
    public ResponseEntity<FormateurResponseDTO> findById(
            @Parameter(description = "ID du Formateur") @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PreAuthorize("@permissionService.isAllowed('Formateur', 'POST')")
    @PostMapping
    public ResponseEntity<FormateurResponseDTO> create(
            @Valid @RequestBody FormateurCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }


    // ─── Departement (ManyToOne) ───
    @Operation(
        summary = "Lister les Formateur par Departement",
        description = "Retourne tous les Formateur liés à un Departement donné"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Formateur', 'GET')")
    @GetMapping("/departement/{departementId}")
    public ResponseEntity<List<FormateurResponseDTO>> getByDepartement(
            @Parameter(description = "ID du Departement") @PathVariable Long departementId) {
        return ResponseEntity.ok(service.findByDepartementId(departementId));
    }

    // ─── Formation (ManyToMany) ───
    @Operation(
        summary = "Lister les Formation de Formateur",
        description = "Retourne tous les Formation associés à un Formateur"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès"),
        @ApiResponse(responseCode = "404", description = "Formateur non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Formateur', 'GET')")
    @GetMapping("/{id}/formations")
    public ResponseEntity<List<FormationResponseDTO>> getFormationsOfFormateur(
            @Parameter(description = "ID du Formateur") @PathVariable Long id) {
        return ResponseEntity.ok(service.findFormationsByFormateurId(id));
    }

    @Operation(
        summary = "Ajouter Formation à Formateur",
        description = "Associe un Formation à un Formateur"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Association effectuée"),
        @ApiResponse(responseCode = "404", description = "Entité non trouvée")
    })
    @PreAuthorize("@permissionService.isAllowed('Formateur', 'POST')")
    @PostMapping("/{formateurId}/formations/{formationId}")
    public ResponseEntity<Void> addFormationToFormateur(
            @Parameter(description = "ID du Formateur") @PathVariable Long formateurId,
            @Parameter(description = "ID du Formation") @PathVariable Long formationId) {
        service.addFormationToFormateur(formateurId, formationId);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Retirer Formation de Formateur",
        description = "Supprime l'association entre un Formation et un Formateur"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Association supprimée"),
        @ApiResponse(responseCode = "404", description = "Entité non trouvée")
    })
    @PreAuthorize("@permissionService.isAllowed('Formateur', 'DELETE')")
    @DeleteMapping("/{formateurId}/formations/{formationId}")
    public ResponseEntity<Void> removeFormationFromFormateur(
            @Parameter(description = "ID du Formateur") @PathVariable Long formateurId,
            @Parameter(description = "ID du Formation") @PathVariable Long formationId) {
        service.removeFormationFromFormateur(formateurId, formationId);
        return ResponseEntity.noContent().build();
    }

    // ─── Session (OneToMany) ───
    @Operation(
        summary = "Lister les Session de Formateur",
        description = "Retourne tous les Session liés à un Formateur donné"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès"),
        @ApiResponse(responseCode = "404", description = "Formateur non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Formateur', 'GET')")
    @GetMapping("/{id}/sessions")
    public ResponseEntity<List<SessionResponseDTO>> getSessionsOfFormateur(
            @Parameter(description = "ID du Formateur") @PathVariable Long id) {
        return ResponseEntity.ok(service.findSessionsByFormateurId(id));
    }

    @Operation(
        summary = "Mettre à jour un Formateur",
        description = "Met à jour les informations d'un Formateur existant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Formateur mis à jour avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Formateur non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Formateur', 'PUT')")
    @PutMapping("/{id}")
    public ResponseEntity<FormateurResponseDTO> update(
            @Parameter(description = "ID du Formateur") @PathVariable Long id,
            @Valid @RequestBody FormateurUpdateDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
        summary = "Supprimer un Formateur",
        description = "Supprime un Formateur selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Formateur supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Formateur non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Formateur', 'DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du Formateur") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
