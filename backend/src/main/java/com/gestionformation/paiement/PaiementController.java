package com.gestionformation.paiement;

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
import com.gestionformation.inscription.InscriptionResponseDTO;
import com.gestionformation.facture.FactureResponseDTO;

@Tag(name = "Paiement", description = "API de gestion des Paiement")
@RestController
@RequestMapping("/api/paiement")
@RequiredArgsConstructor
public class PaiementController {

    private final PaiementService service;

    private final SecurityUtils securityUtils;

    @Operation(
        summary = "Lister tous les Paiement",
        description = "Retourne la liste complète des Paiement"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Paiement', 'GET')")
    @GetMapping
    public ResponseEntity<List<PaiementResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
        summary = "Compter les Paiement",
        description = "Retourne le nombre total de Paiement"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nombre retourné avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Paiement', 'GET')")
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @Operation(summary = "Statistiques Paiement par modePaiement")
    @PreAuthorize("@permissionService.isAllowed('Paiement', 'GET')")
    @GetMapping("/stats/by-modePaiement")
    public ResponseEntity<java.util.Map<String, Long>> statsByModePaiement() {
        return ResponseEntity.ok(service.countByModePaiement());
    }

    @Operation(
        summary = "Trouver un Paiement par ID",
        description = "Retourne un Paiement selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paiement trouvé"),
        @ApiResponse(responseCode = "404", description = "Paiement non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Paiement', 'GET')")
    @GetMapping("/{id}")
    public ResponseEntity<PaiementResponseDTO> findById(
            @Parameter(description = "ID du Paiement") @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PreAuthorize("@permissionService.isAllowed('Paiement', 'POST')")
    @PostMapping
    public ResponseEntity<PaiementResponseDTO> create(
            @Valid @RequestBody PaiementCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }


    // ─── Inscription (OneToOne) ───
    @Operation(
        summary = "Obtenir le Inscription d'un Paiement",
        description = "Retourne le Inscription associé à un Paiement"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inscription retourné avec succès"),
        @ApiResponse(responseCode = "404", description = "Paiement non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Paiement', 'GET')")
    @GetMapping("/{id}/inscription")
    public ResponseEntity<InscriptionResponseDTO> getInscriptionOfPaiement(
            @Parameter(description = "ID du Paiement") @PathVariable Long id) {
        return ResponseEntity.ok(service.findInscriptionByPaiementId(id));
    }

    // ─── Facture (OneToOne) ───
    @Operation(
        summary = "Obtenir le Facture d'un Paiement",
        description = "Retourne le Facture associé à un Paiement"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Facture retourné avec succès"),
        @ApiResponse(responseCode = "404", description = "Paiement non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Paiement', 'GET')")
    @GetMapping("/{id}/facture")
    public ResponseEntity<FactureResponseDTO> getFactureOfPaiement(
            @Parameter(description = "ID du Paiement") @PathVariable Long id) {
        return ResponseEntity.ok(service.findFactureByPaiementId(id));
    }

    @Operation(
        summary = "Mettre à jour un Paiement",
        description = "Met à jour les informations d'un Paiement existant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paiement mis à jour avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Paiement non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Paiement', 'PUT')")
    @PutMapping("/{id}")
    public ResponseEntity<PaiementResponseDTO> update(
            @Parameter(description = "ID du Paiement") @PathVariable Long id,
            @Valid @RequestBody PaiementUpdateDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
        summary = "Supprimer un Paiement",
        description = "Supprime un Paiement selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Paiement supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Paiement non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Paiement', 'DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du Paiement") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
