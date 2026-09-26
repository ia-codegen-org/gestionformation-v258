package com.gestionformation.salle;

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
import com.gestionformation.batiment.BatimentResponseDTO;
import com.gestionformation.session.SessionResponseDTO;

@Tag(name = "Salle", description = "API de gestion des Salle")
@RestController
@RequestMapping("/api/salle")
@RequiredArgsConstructor
public class SalleController {

    private final SalleService service;

    private final SecurityUtils securityUtils;

    @Operation(
        summary = "Lister tous les Salle",
        description = "Retourne la liste complète des Salle"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Salle', 'GET')")
    @GetMapping
    public ResponseEntity<List<SalleResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
        summary = "Compter les Salle",
        description = "Retourne le nombre total de Salle"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nombre retourné avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Salle', 'GET')")
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @Operation(
        summary = "Trouver un Salle par ID",
        description = "Retourne un Salle selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Salle trouvé"),
        @ApiResponse(responseCode = "404", description = "Salle non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Salle', 'GET')")
    @GetMapping("/{id}")
    public ResponseEntity<SalleResponseDTO> findById(
            @Parameter(description = "ID du Salle") @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PreAuthorize("@permissionService.isAllowed('Salle', 'POST')")
    @PostMapping
    public ResponseEntity<SalleResponseDTO> create(
            @Valid @RequestBody SalleCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }


    // ─── Batiment (ManyToOne) ───
    @Operation(
        summary = "Lister les Salle par Batiment",
        description = "Retourne tous les Salle liés à un Batiment donné"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Salle', 'GET')")
    @GetMapping("/batiment/{batimentId}")
    public ResponseEntity<List<SalleResponseDTO>> getByBatiment(
            @Parameter(description = "ID du Batiment") @PathVariable Long batimentId) {
        return ResponseEntity.ok(service.findByBatimentId(batimentId));
    }

    // ─── Session (OneToMany) ───
    @Operation(
        summary = "Lister les Session de Salle",
        description = "Retourne tous les Session liés à un Salle donné"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès"),
        @ApiResponse(responseCode = "404", description = "Salle non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Salle', 'GET')")
    @GetMapping("/{id}/sessions")
    public ResponseEntity<List<SessionResponseDTO>> getSessionsOfSalle(
            @Parameter(description = "ID du Salle") @PathVariable Long id) {
        return ResponseEntity.ok(service.findSessionsBySalleId(id));
    }

    @Operation(
        summary = "Mettre à jour un Salle",
        description = "Met à jour les informations d'un Salle existant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Salle mis à jour avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Salle non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Salle', 'PUT')")
    @PutMapping("/{id}")
    public ResponseEntity<SalleResponseDTO> update(
            @Parameter(description = "ID du Salle") @PathVariable Long id,
            @Valid @RequestBody SalleUpdateDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
        summary = "Supprimer un Salle",
        description = "Supprime un Salle selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Salle supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Salle non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Salle', 'DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du Salle") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
