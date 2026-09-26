package com.gestionformation.departement;

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
import com.gestionformation.formateur.FormateurResponseDTO;

@Tag(name = "Departement", description = "API de gestion des Departement")
@RestController
@RequestMapping("/api/departement")
@RequiredArgsConstructor
public class DepartementController {

    private final DepartementService service;

    private final SecurityUtils securityUtils;

    @Operation(
        summary = "Lister tous les Departement",
        description = "Retourne la liste complète des Departement"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Departement', 'GET')")
    @GetMapping
    public ResponseEntity<List<DepartementResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
        summary = "Compter les Departement",
        description = "Retourne le nombre total de Departement"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nombre retourné avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('Departement', 'GET')")
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @Operation(
        summary = "Trouver un Departement par ID",
        description = "Retourne un Departement selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Departement trouvé"),
        @ApiResponse(responseCode = "404", description = "Departement non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Departement', 'GET')")
    @GetMapping("/{id}")
    public ResponseEntity<DepartementResponseDTO> findById(
            @Parameter(description = "ID du Departement") @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PreAuthorize("@permissionService.isAllowed('Departement', 'POST')")
    @PostMapping
    public ResponseEntity<DepartementResponseDTO> create(
            @Valid @RequestBody DepartementCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }


    // ─── Formateur (OneToMany) ───
    @Operation(
        summary = "Lister les Formateur de Departement",
        description = "Retourne tous les Formateur liés à un Departement donné"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès"),
        @ApiResponse(responseCode = "404", description = "Departement non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Departement', 'GET')")
    @GetMapping("/{id}/formateurs")
    public ResponseEntity<List<FormateurResponseDTO>> getFormateursOfDepartement(
            @Parameter(description = "ID du Departement") @PathVariable Long id) {
        return ResponseEntity.ok(service.findFormateursByDepartementId(id));
    }

    @Operation(
        summary = "Mettre à jour un Departement",
        description = "Met à jour les informations d'un Departement existant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Departement mis à jour avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "Departement non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Departement', 'PUT')")
    @PutMapping("/{id}")
    public ResponseEntity<DepartementResponseDTO> update(
            @Parameter(description = "ID du Departement") @PathVariable Long id,
            @Valid @RequestBody DepartementUpdateDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
        summary = "Supprimer un Departement",
        description = "Supprime un Departement selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Departement supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Departement non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('Departement', 'DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du Departement") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
