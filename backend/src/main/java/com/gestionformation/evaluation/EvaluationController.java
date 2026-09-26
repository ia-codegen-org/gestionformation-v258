package com.gestionformation.evaluation;

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

@Tag(name = "Evaluation", description = "API de gestion des Evaluation")
@RestController
@RequestMapping("/api/evaluation")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService service;

    private final SecurityUtils securityUtils;

    @Operation(
        summary = "Lister tous les Evaluation",
        description = "Retourne la liste complète des Evaluation"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Evaluation', 'GET')")
    @GetMapping
    public ResponseEntity<List<EvaluationResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
        summary = "Compter les Evaluation",
        description = "Retourne le nombre total de Evaluation"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nombre retourné avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Evaluation', 'GET')")
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @Operation(
        summary = "Trouver un Evaluation par ID",
        description = "Retourne un Evaluation selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evaluation trouvé"),
        @ApiResponse(responseCode = "404", description = "Evaluation non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Evaluation', 'GET')")
    @GetMapping("/{id}")
    public ResponseEntity<EvaluationResponseDTO> findById(
            @Parameter(description = "ID du Evaluation") @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PreAuthorize("@permissionService.isAllowed('Evaluation', 'POST')")
    @PostMapping
    public ResponseEntity<EvaluationResponseDTO> create(
            @Valid @RequestBody EvaluationCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }


    // ─── Etudiant (ManyToOne) ───
    @Operation(
        summary = "Lister les Evaluation par Etudiant",
        description = "Retourne tous les Evaluation liés à un Etudiant donné"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Evaluation', 'GET')")
    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<EvaluationResponseDTO>> getByEtudiant(
            @Parameter(description = "ID du Etudiant") @PathVariable Long etudiantId) {
        return ResponseEntity.ok(service.findByEtudiantId(etudiantId));
    }

    // ─── Session (ManyToOne) ───
    @Operation(
        summary = "Lister les Evaluation par Session",
        description = "Retourne tous les Evaluation liés à un Session donné"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Evaluation', 'GET')")
    @GetMapping("/session/{sessionId}")
    public ResponseEntity<List<EvaluationResponseDTO>> getBySession(
            @Parameter(description = "ID du Session") @PathVariable Long sessionId) {
        return ResponseEntity.ok(service.findBySessionId(sessionId));
    }

    @Operation(
        summary = "Mettre à jour un Evaluation",
        description = "Met à jour les informations d'un Evaluation existant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Evaluation mis à jour avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Evaluation non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Evaluation', 'PUT')")
    @PutMapping("/{id}")
    public ResponseEntity<EvaluationResponseDTO> update(
            @Parameter(description = "ID du Evaluation") @PathVariable Long id,
            @Valid @RequestBody EvaluationUpdateDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
        summary = "Supprimer un Evaluation",
        description = "Supprime un Evaluation selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Evaluation supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Evaluation non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Evaluation', 'DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du Evaluation") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
