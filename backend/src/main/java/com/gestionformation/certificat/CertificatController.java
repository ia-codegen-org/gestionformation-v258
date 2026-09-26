package com.gestionformation.certificat;

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

@Tag(name = "Certificat", description = "API de gestion des Certificat")
@RestController
@RequestMapping("/api/certificat")
@RequiredArgsConstructor
public class CertificatController {

    private final CertificatService service;

    private final SecurityUtils securityUtils;

    @Operation(
        summary = "Lister tous les Certificat",
        description = "Retourne la liste complète des Certificat"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Certificat', 'GET')")
    @GetMapping
    public ResponseEntity<List<CertificatResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
        summary = "Compter les Certificat",
        description = "Retourne le nombre total de Certificat"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nombre retourné avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Certificat', 'GET')")
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @Operation(summary = "Statistiques Certificat par mention")
    @PreAuthorize("@permissionService.isAllowed('Certificat', 'GET')")
    @GetMapping("/stats/by-mention")
    public ResponseEntity<java.util.Map<String, Long>> statsByMention() {
        return ResponseEntity.ok(service.countByMention());
    }

    @Operation(
        summary = "Trouver un Certificat par ID",
        description = "Retourne un Certificat selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Certificat trouvé"),
        @ApiResponse(responseCode = "404", description = "Certificat non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Certificat', 'GET')")
    @GetMapping("/{id}")
    public ResponseEntity<CertificatResponseDTO> findById(
            @Parameter(description = "ID du Certificat") @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PreAuthorize("@permissionService.isAllowed('Certificat', 'POST')")
    @PostMapping
    public ResponseEntity<CertificatResponseDTO> create(
            @Valid @RequestBody CertificatCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }


    // ─── Etudiant (ManyToOne) ───
    @Operation(
        summary = "Lister les Certificat par Etudiant",
        description = "Retourne tous les Certificat liés à un Etudiant donné"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Certificat', 'GET')")
    @GetMapping("/etudiant/{etudiantId}")
    public ResponseEntity<List<CertificatResponseDTO>> getByEtudiant(
            @Parameter(description = "ID du Etudiant") @PathVariable Long etudiantId) {
        return ResponseEntity.ok(service.findByEtudiantId(etudiantId));
    }

    @Operation(
        summary = "Mettre à jour un Certificat",
        description = "Met à jour les informations d'un Certificat existant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Certificat mis à jour avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Certificat non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Certificat', 'PUT')")
    @PutMapping("/{id}")
    public ResponseEntity<CertificatResponseDTO> update(
            @Parameter(description = "ID du Certificat") @PathVariable Long id,
            @Valid @RequestBody CertificatUpdateDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
        summary = "Supprimer un Certificat",
        description = "Supprime un Certificat selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Certificat supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Certificat non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Certificat', 'DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du Certificat") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
