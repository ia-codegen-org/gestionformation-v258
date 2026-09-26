package com.gestionformation.inscription;

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
import com.gestionformation.etudiant.EtudiantResponseDTO;
import com.gestionformation.session.SessionResponseDTO;
import com.gestionformation.paiement.PaiementResponseDTO;

@Tag(name = "Inscription", description = "API de gestion des Inscription")
@RestController
@RequestMapping("/api/inscription")
@RequiredArgsConstructor
public class InscriptionController {

    private final InscriptionService service;

    private final SecurityUtils securityUtils;

    @Operation(
        summary = "Lister tous les Inscription",
        description = "Retourne la liste complète des Inscription"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Inscription', 'GET')")
    @GetMapping
    public ResponseEntity<List<InscriptionResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
        summary = "Compter les Inscription",
        description = "Retourne le nombre total de Inscription"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nombre retourné avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Inscription', 'GET')")
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @Operation(summary = "Statistiques Inscription par statut")
    @PreAuthorize("@permissionService.isAllowed('Inscription', 'GET')")
    @GetMapping("/stats/by-statut")
    public ResponseEntity<java.util.Map<String, Long>> statsByStatut() {
        return ResponseEntity.ok(service.countByStatut());
    }

    @Operation(
        summary = "Trouver un Inscription par ID",
        description = "Retourne un Inscription selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inscription trouvé"),
        @ApiResponse(responseCode = "404", description = "Inscription non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Inscription', 'GET')")
    @GetMapping("/{id}")
    public ResponseEntity<InscriptionResponseDTO> findById(
            @Parameter(description = "ID du Inscription") @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PreAuthorize("@permissionService.isAllowed('Inscription', 'POST')")
    @PostMapping
    public ResponseEntity<InscriptionResponseDTO> create(
            @Valid @RequestBody InscriptionCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }


    // ─── Etudiant (ManyToOne) ───
    @Operation(
        summary = "Lister les Inscription par Etudiant",
        description = "Retourne tous les Inscription liés à un Etudiant donné"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Inscription', 'GET')")
    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<InscriptionResponseDTO>> getByEtudiant(
            @Parameter(description = "ID du Etudiant") @PathVariable Long etudiantId) {
        return ResponseEntity.ok(service.findByEtudiantId(etudiantId));
    }

    // ─── Session (ManyToOne) ───
    @Operation(
        summary = "Lister les Inscription par Session",
        description = "Retourne tous les Inscription liés à un Session donné"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Inscription', 'GET')")
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<InscriptionResponseDTO>> getBySession(
            @Parameter(description = "ID du Session") @PathVariable Long sessionId) {
        return ResponseEntity.ok(service.findBySessionId(sessionId));
    }

    // ─── Paiement (OneToOne) ───
    @Operation(
        summary = "Obtenir le Paiement d'un Inscription",
        description = "Retourne le Paiement associé à un Inscription"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Paiement retourné avec succès"),
        @ApiResponse(responseCode = "404", description = "Inscription non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Inscription', 'GET')")
    @GetMapping("/{id}/paiement")
    public ResponseEntity<PaiementResponseDTO> getPaiementOfInscription(
            @Parameter(description = "ID du Inscription") @PathVariable Long id) {
        return ResponseEntity.ok(service.findPaiementByInscriptionId(id));
    }

    @Operation(
        summary = "Mettre à jour un Inscription",
        description = "Met à jour les informations d'un Inscription existant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inscription mis à jour avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Inscription non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Inscription', 'PUT')")
    @PutMapping("/{id}")
    public ResponseEntity<InscriptionResponseDTO> update(
            @Parameter(description = "ID du Inscription") @PathVariable Long id,
            @Valid @RequestBody InscriptionUpdateDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
        summary = "Supprimer un Inscription",
        description = "Supprime un Inscription selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Inscription supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Inscription non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Inscription', 'DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du Inscription") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
