package com.gestionformation.categorie;

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
import com.gestionformation.formation.FormationResponseDTO;

@Tag(name = "Categorie", description = "API de gestion des Categorie")
@RestController
@RequestMapping("/api/categorie")
@RequiredArgsConstructor
public class CategorieController {

    private final CategorieService service;

    private final SecurityUtils securityUtils;

    @Operation(
        summary = "Lister tous les Categorie",
        description = "Retourne la liste complète des Categorie"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Categorie', 'GET')")
    @GetMapping
    public ResponseEntity<List<CategorieResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
        summary = "Compter les Categorie",
        description = "Retourne le nombre total de Categorie"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nombre retourné avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Categorie', 'GET')")
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @Operation(
        summary = "Trouver un Categorie par ID",
        description = "Retourne un Categorie selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categorie trouvé"),
        @ApiResponse(responseCode = "404", description = "Categorie non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Categorie', 'GET')")
    @GetMapping("/{id}")
    public ResponseEntity<CategorieResponseDTO> findById(
            @Parameter(description = "ID du Categorie") @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PreAuthorize("@permissionService.isAllowed('Categorie', 'POST')")
    @PostMapping
    public ResponseEntity<CategorieResponseDTO> create(
            @Valid @RequestBody CategorieCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }


    // ─── Formation (OneToMany) ───
    @Operation(
        summary = "Lister les Formation de Categorie",
        description = "Retourne tous les Formation liés à un Categorie donné"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès"),
        @ApiResponse(responseCode = "404", description = "Categorie non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Categorie', 'GET')")
    @GetMapping("/{id}/formations")
    public ResponseEntity<List<FormationResponseDTO>> getFormationsOfCategorie(
            @Parameter(description = "ID du Categorie") @PathVariable Long id) {
        return ResponseEntity.ok(service.findFormationsByCategorieId(id));
    }

    @Operation(
        summary = "Mettre à jour un Categorie",
        description = "Met à jour les informations d'un Categorie existant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Categorie mis à jour avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Categorie non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Categorie', 'PUT')")
    @PutMapping("/{id}")
    public ResponseEntity<CategorieResponseDTO> update(
            @Parameter(description = "ID du Categorie") @PathVariable Long id,
            @Valid @RequestBody CategorieUpdateDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
        summary = "Supprimer un Categorie",
        description = "Supprime un Categorie selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Categorie supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Categorie non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Categorie', 'DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du Categorie") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
