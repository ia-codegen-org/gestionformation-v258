package com.gestionformation.batiment;

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
import com.gestionformation.salle.SalleResponseDTO;

@Tag(name = "Batiment", description = "API de gestion des Batiment")
@RestController
@RequestMapping("/api/batiment")
@RequiredArgsConstructor
public class BatimentController {

    private final BatimentService service;

    private final SecurityUtils securityUtils;

    @Operation(
        summary = "Lister tous les Batiment",
        description = "Retourne la liste complète des Batiment"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Batiment', 'GET')")
    @GetMapping
    public ResponseEntity<List<BatimentResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
        summary = "Compter les Batiment",
        description = "Retourne le nombre total de Batiment"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nombre retourné avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Batiment', 'GET')")
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @Operation(
        summary = "Trouver un Batiment par ID",
        description = "Retourne un Batiment selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Batiment trouvé"),
        @ApiResponse(responseCode = "404", description = "Batiment non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Batiment', 'GET')")
    @GetMapping("/{id}")
    public ResponseEntity<BatimentResponseDTO> findById(
            @Parameter(description = "ID du Batiment") @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PreAuthorize("@permissionService.isAllowed('Batiment', 'POST')")
    @PostMapping
    public ResponseEntity<BatimentResponseDTO> create(
            @Valid @RequestBody BatimentCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }


    // ─── Salle (OneToMany) ───
    @Operation(
        summary = "Lister les Salle de Batiment",
        description = "Retourne tous les Salle liés à un Batiment donné"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès"),
        @ApiResponse(responseCode = "404", description = "Batiment non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Batiment', 'GET')")
    @GetMapping("/{id}/salles")
    public ResponseEntity<List<SalleResponseDTO>> getSallesOfBatiment(
            @Parameter(description = "ID du Batiment") @PathVariable Long id) {
        return ResponseEntity.ok(service.findSallesByBatimentId(id));
    }

    @Operation(
        summary = "Mettre à jour un Batiment",
        description = "Met à jour les informations d'un Batiment existant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Batiment mis à jour avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Batiment non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Batiment', 'PUT')")
    @PutMapping("/{id}")
    public ResponseEntity<BatimentResponseDTO> update(
            @Parameter(description = "ID du Batiment") @PathVariable Long id,
            @Valid @RequestBody BatimentUpdateDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
        summary = "Supprimer un Batiment",
        description = "Supprime un Batiment selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Batiment supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Batiment non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Batiment', 'DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du Batiment") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
