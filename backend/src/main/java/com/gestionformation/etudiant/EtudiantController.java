package com.gestionformation.etudiant;

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
import com.gestionformation.session.SessionResponseDTO;
import com.gestionformation.evaluation.EvaluationResponseDTO;
import com.gestionformation.certificat.CertificatResponseDTO;
import com.gestionformation.inscription.InscriptionResponseDTO;

@Tag(name = "Etudiant", description = "API de gestion des Etudiant")
@RestController
@RequestMapping("/api/etudiant")
@RequiredArgsConstructor
public class EtudiantController {

    private final EtudiantService service;

    private final SecurityUtils securityUtils;

    @Operation(
        summary = "Lister tous les Etudiant",
        description = "Retourne la liste complète des Etudiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Etudiant', 'GET')")
    @GetMapping
    public ResponseEntity<List<EtudiantResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
        summary = "Compter les Etudiant",
        description = "Retourne le nombre total de Etudiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nombre retourné avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Etudiant', 'GET')")
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @Operation(
        summary = "Trouver un Etudiant par ID",
        description = "Retourne un Etudiant selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Etudiant trouvé"),
        @ApiResponse(responseCode = "404", description = "Etudiant non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Etudiant', 'GET')")
    @GetMapping("/{id}")
    public ResponseEntity<EtudiantResponseDTO> findById(
            @Parameter(description = "ID du Etudiant") @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PreAuthorize("@permissionService.isAllowed('Etudiant', 'POST')")
    @PostMapping
    public ResponseEntity<EtudiantResponseDTO> create(
            @Valid @RequestBody EtudiantCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }


    // ─── Session (ManyToMany) ───
    @Operation(
        summary = "Lister les Session de Etudiant",
        description = "Retourne tous les Session associés à un Etudiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès"),
        @ApiResponse(responseCode = "404", description = "Etudiant non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Etudiant', 'GET')")
    @GetMapping("/{id}/sessions")
    public ResponseEntity<List<SessionResponseDTO>> getSessionsOfEtudiant(
            @Parameter(description = "ID du Etudiant") @PathVariable Long id) {
        return ResponseEntity.ok(service.findSessionsByEtudiantId(id));
    }

    @Operation(
        summary = "Ajouter Session à Etudiant",
        description = "Associe un Session à un Etudiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Association effectuée"),
        @ApiResponse(responseCode = "404", description = "Entité non trouvée")
    })
    @PreAuthorize("@permissionService.isAllowed('Etudiant', 'POST')")
    @PostMapping("/{etudiantId}/sessions/{sessionId}")
    public ResponseEntity<Void> addSessionToEtudiant(
            @Parameter(description = "ID du Etudiant") @PathVariable Long etudiantId,
            @Parameter(description = "ID du Session") @PathVariable Long sessionId) {
        service.addSessionToEtudiant(etudiantId, sessionId);
        return ResponseEntity.ok().build();
    }

    @Operation(
        summary = "Retirer Session de Etudiant",
        description = "Supprime l'association entre un Session et un Etudiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Association supprimée"),
        @ApiResponse(responseCode = "404", description = "Entité non trouvée")
    })
    @PreAuthorize("@permissionService.isAllowed('Etudiant', 'DELETE')")
    @DeleteMapping("/{etudiantId}/sessions/{sessionId}")
    public ResponseEntity<Void> removeSessionFromEtudiant(
            @Parameter(description = "ID du Etudiant") @PathVariable Long etudiantId,
            @Parameter(description = "ID du Session") @PathVariable Long sessionId) {
        service.removeSessionFromEtudiant(etudiantId, sessionId);
        return ResponseEntity.noContent().build();
    }

    // ─── Evaluation (OneToMany) ───
    @Operation(
        summary = "Lister les Evaluation de Etudiant",
        description = "Retourne tous les Evaluation liés à un Etudiant donné"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès"),
        @ApiResponse(responseCode = "404", description = "Etudiant non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Etudiant', 'GET')")
    @GetMapping("/{id}/evaluations")
    public ResponseEntity<List<EvaluationResponseDTO>> getEvaluationsOfEtudiant(
            @Parameter(description = "ID du Etudiant") @PathVariable Long id) {
        return ResponseEntity.ok(service.findEvaluationsByEtudiantId(id));
    }

    // ─── Certificat (OneToMany) ───
    @Operation(
        summary = "Lister les Certificat de Etudiant",
        description = "Retourne tous les Certificat liés à un Etudiant donné"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès"),
        @ApiResponse(responseCode = "404", description = "Etudiant non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Etudiant', 'GET')")
    @GetMapping("/{id}/certificats")
    public ResponseEntity<List<CertificatResponseDTO>> getCertificatsOfEtudiant(
            @Parameter(description = "ID du Etudiant") @PathVariable Long id) {
        return ResponseEntity.ok(service.findCertificatsByEtudiantId(id));
    }

    // ─── Inscription (OneToMany) ───
    @Operation(
        summary = "Lister les Inscription de Etudiant",
        description = "Retourne tous les Inscription liés à un Etudiant donné"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès"),
        @ApiResponse(responseCode = "404", description = "Etudiant non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Etudiant', 'GET')")
    @GetMapping("/{id}/inscriptions")
    public ResponseEntity<List<InscriptionResponseDTO>> getInscriptionsOfEtudiant(
            @Parameter(description = "ID du Etudiant") @PathVariable Long id) {
        return ResponseEntity.ok(service.findInscriptionsByEtudiantId(id));
    }

    @Operation(
        summary = "Mettre à jour un Etudiant",
        description = "Met à jour les informations d'un Etudiant existant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Etudiant mis à jour avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Etudiant non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Etudiant', 'PUT')")
    @PutMapping("/{id}")
    public ResponseEntity<EtudiantResponseDTO> update(
            @Parameter(description = "ID du Etudiant") @PathVariable Long id,
            @Valid @RequestBody EtudiantUpdateDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
        summary = "Supprimer un Etudiant",
        description = "Supprime un Etudiant selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Etudiant supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Etudiant non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Etudiant', 'DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du Etudiant") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
