package com.gestionformation.session;

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
import com.gestionformation.formateur.FormateurResponseDTO;
import com.gestionformation.salle.SalleResponseDTO;
import com.gestionformation.etudiant.EtudiantResponseDTO;
import com.gestionformation.inscription.InscriptionResponseDTO;
import com.gestionformation.evaluation.EvaluationResponseDTO;

@Tag(name = "Session", description = "API de gestion des Session")
@RestController
@RequestMapping("/api/session")
@RequiredArgsConstructor
public class SessionController {

    private final SessionService service;

    private final SecurityUtils securityUtils;

    @Operation(
        summary = "Lister tous les Session",
        description = "Retourne la liste complète des Session"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Session', 'GET')")
    @GetMapping
    public ResponseEntity<List<SessionResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
        summary = "Compter les Session",
        description = "Retourne le nombre total de Session"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nombre retourné avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Session', 'GET')")
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @Operation(
        summary = "Trouver un Session par ID",
        description = "Retourne un Session selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Session trouvé"),
        @ApiResponse(responseCode = "404", description = "Session non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Session', 'GET')")
    @GetMapping("/{id}")
    public ResponseEntity<SessionResponseDTO> findById(
            @Parameter(description = "ID du Session") @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PreAuthorize("@permissionService.isAllowed('Session', 'POST')")
    @PostMapping
    public ResponseEntity<SessionResponseDTO> create(
            @Valid @RequestBody SessionCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }


    // ─── Formation (ManyToOne) ───
    @Operation(
        summary = "Lister les Session par Formation",
        description = "Retourne tous les Session liés à un Formation donné"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Session', 'GET')")
    @GetMapping("/formation/{formationId}")
    public ResponseEntity<List<SessionResponseDTO>> getByFormation(
            @Parameter(description = "ID du Formation") @PathVariable Long formationId) {
        return ResponseEntity.ok(service.findByFormationId(formationId));
    }

    // ─── Formateur (ManyToOne) ───
    @Operation(
        summary = "Lister les Session par Formateur",
        description = "Retourne tous les Session liés à un Formateur donné"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Session', 'GET')")
    @GetMapping("/formateur/{formateurId}")
    public ResponseEntity<List<SessionResponseDTO>> getByFormateur(
            @Parameter(description = "ID du Formateur") @PathVariable Long formateurId) {
        return ResponseEntity.ok(service.findByFormateurId(formateurId));
    }

    // ─── Salle (ManyToOne) ───
    @Operation(
        summary = "Lister les Session par Salle",
        description = "Retourne tous les Session liés à un Salle donné"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Session', 'GET')")
    @GetMapping("/salle/{salleId}")
    public ResponseEntity<List<SessionResponseDTO>> getBySalle(
            @Parameter(description = "ID du Salle") @PathVariable Long salleId) {
        return ResponseEntity.ok(service.findBySalleId(salleId));
    }

    // ─── Etudiant (ManyToMany) ───
    @Operation(
        summary = "Lister les Etudiant de Session",
        description = "Retourne tous les Etudiant associés à un Session"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès"),
        @ApiResponse(responseCode = "404", description = "Session non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Session', 'GET')")
    @GetMapping("/{id}/etudiants")
    public ResponseEntity<List<EtudiantResponseDTO>> getEtudiantsOfSession(
            @Parameter(description = "ID du Session") @PathVariable Long id) {
        return ResponseEntity.ok(service.findEtudiantsBySessionId(id));
    }

    // ─── Inscription (OneToMany) ───
    @Operation(
        summary = "Lister les Inscription de Session",
        description = "Retourne tous les Inscription liés à un Session donné"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès"),
        @ApiResponse(responseCode = "404", description = "Session non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Session', 'GET')")
    @GetMapping("/{id}/inscriptions")
    public ResponseEntity<List<InscriptionResponseDTO>> getInscriptionsOfSession(
            @Parameter(description = "ID du Session") @PathVariable Long id) {
        return ResponseEntity.ok(service.findInscriptionsBySessionId(id));
    }

    // ─── Evaluation (OneToMany) ───
    @Operation(
        summary = "Lister les Evaluation de Session",
        description = "Retourne tous les Evaluation liés à un Session donné"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès"),
        @ApiResponse(responseCode = "404", description = "Session non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Session', 'GET')")
    @GetMapping("/{id}/evaluations")
    public ResponseEntity<List<EvaluationResponseDTO>> getEvaluationsOfSession(
            @Parameter(description = "ID du Session") @PathVariable Long id) {
        return ResponseEntity.ok(service.findEvaluationsBySessionId(id));
    }

    @Operation(
        summary = "Mettre à jour un Session",
        description = "Met à jour les informations d'un Session existant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Session mis à jour avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Session non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Session', 'PUT')")
    @PutMapping("/{id}")
    public ResponseEntity<SessionResponseDTO> update(
            @Parameter(description = "ID du Session") @PathVariable Long id,
            @Valid @RequestBody SessionUpdateDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
        summary = "Supprimer un Session",
        description = "Supprime un Session selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Session supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Session non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Session', 'DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du Session") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
