package com.gestionformation.facture;

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
import com.gestionformation.paiement.PaiementResponseDTO;

@Tag(name = "Facture", description = "API de gestion des Facture")
@RestController
@RequestMapping("/api/facture")
@RequiredArgsConstructor
public class FactureController {

    private final FactureService service;

    private final SecurityUtils securityUtils;

    @Operation(
        summary = "Lister tous les Facture",
        description = "Retourne la liste complète des Facture"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Facture', 'GET')")
    @GetMapping
    public ResponseEntity<List<FactureResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
        summary = "Compter les Facture",
        description = "Retourne le nombre total de Facture"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nombre retourné avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Facture', 'GET')")
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @Operation(summary = "Statistiques Facture par statut")
    @PreAuthorize("@permissionService.isAllowed('Facture', 'GET')")
    @GetMapping("/stats/by-statut")
    public ResponseEntity<java.util.Map<String, Long>> statsByStatut() {
        return ResponseEntity.ok(service.countByStatut());
    }

    @Operation(
        summary = "Trouver un Facture par ID",
        description = "Retourne un Facture selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Facture trouvé"),
        @ApiResponse(responseCode = "404", description = "Facture non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Facture', 'GET')")
    @GetMapping("/{id}")
    public ResponseEntity<FactureResponseDTO> findById(
            @Parameter(description = "ID du Facture") @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PreAuthorize("@permissionService.isAllowed('Facture', 'POST')")
    @PostMapping
    public ResponseEntity<FactureResponseDTO> create(
            @Valid @RequestBody FactureCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }


    // ─── Paiement (OneToOne) ───
    @Operation(
        summary = "Obtenir le Paiement d'un Facture",
        description = "Retourne le Paiement associé à un Facture"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paiement retourné avec succès"),
        @ApiResponse(responseCode = "404", description = "Facture non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Facture', 'GET')")
    @GetMapping("/{id}/paiement")
    public ResponseEntity<PaiementResponseDTO> getPaiementOfFacture(
            @Parameter(description = "ID du Facture") @PathVariable Long id) {
        return ResponseEntity.ok(service.findPaiementByFactureId(id));
    }

    @Operation(
        summary = "Mettre à jour un Facture",
        description = "Met à jour les informations d'un Facture existant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Facture mis à jour avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Facture non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Facture', 'PUT')")
    @PutMapping("/{id}")
    public ResponseEntity<FactureResponseDTO> update(
            @Parameter(description = "ID du Facture") @PathVariable Long id,
            @Valid @RequestBody FactureUpdateDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
        summary = "Supprimer un Facture",
        description = "Supprime un Facture selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Facture supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Facture non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Facture', 'DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du Facture") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
