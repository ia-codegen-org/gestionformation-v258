package com.gestionformation.formation;

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
import com.gestionformation.categorie.CategorieResponseDTO;
import com.gestionformation.formateur.FormateurResponseDTO;
import com.gestionformation.session.SessionResponseDTO;

@Tag(name = "Formation", description = "API de gestion des Formation")
@RestController
@RequestMapping("/api/formation")
@RequiredArgsConstructor
public class FormationController {

    private final FormationService service;

    private final SecurityUtils securityUtils;

    @Operation(
        summary = "Lister tous les Formation",
        description = "Retourne la liste complète des Formation"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Formation', 'GET')")
    @GetMapping
    public ResponseEntity<List<FormationResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
        summary = "Compter les Formation",
        description = "Retourne le nombre total de Formation"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nombre retourné avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Formation', 'GET')")
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @Operation(
        summary = "Trouver un Formation par ID",
        description = "Retourne un Formation selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Formation trouvé"),
        @ApiResponse(responseCode = "404", description = "Formation non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Formation', 'GET')")
    @GetMapping("/{id}")
    public ResponseEntity<FormationResponseDTO> findById(
            @Parameter(description = "ID du Formation") @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PreAuthorize("@permissionService.isAllowed('Formation', 'POST')")
    @PostMapping
    public ResponseEntity<FormationResponseDTO> create(
            @Valid @RequestBody FormationCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }


    // ─── Categorie (ManyToOne) ───
    @Operation(
        summary = "Lister les Formation par Categorie",
        description = "Retourne tous les Formation liés à un Categorie donné"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Formation', 'GET')")
    @GetMapping("/categorie/{categorieId}")
    public ResponseEntity<List<FormationResponseDTO>> getByCategorie(
            @Parameter(description = "ID du Categorie") @PathVariable Long categorieId) {
        return ResponseEntity.ok(service.findByCategorieId(categorieId));
    }

    // ─── Formateur (ManyToMany) ───
    @Operation(
        summary = "Lister les Formateur de Formation",
        description = "Retourne tous les Formateur associés à un Formation"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès"),
        @ApiResponse(responseCode = "404", description = "Formation non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Formation', 'GET')")
    @GetMapping("/{id}/formateurs")
    public ResponseEntity<List<FormateurResponseDTO>> getFormateursOfFormation(
            @Parameter(description = "ID du Formation") @PathVariable Long id) {
        return ResponseEntity.ok(service.findFormateursByFormationId(id));
    }

    // ─── Session (OneToMany) ───
    @Operation(
        summary = "Lister les Session de Formation",
        description = "Retourne tous les Session liés à un Formation donné"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès"),
        @ApiResponse(responseCode = "404", description = "Formation non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Formation', 'GET')")
    @GetMapping("/{id}/sessions")
    public ResponseEntity<List<SessionResponseDTO>> getSessionsOfFormation(
            @Parameter(description = "ID du Formation") @PathVariable Long id) {
        return ResponseEntity.ok(service.findSessionsByFormationId(id));
    }

    @Operation(
        summary = "Mettre à jour un Formation",
        description = "Met à jour les informations d'un Formation existant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Formation mis à jour avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Formation non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Formation', 'PUT')")
    @PutMapping("/{id}")
    public ResponseEntity<FormationResponseDTO> update(
            @Parameter(description = "ID du Formation") @PathVariable Long id,
            @Valid @RequestBody FormationUpdateDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
        summary = "Supprimer un Formation",
        description = "Supprime un Formation selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Formation supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Formation non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Formation', 'DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du Formation") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
