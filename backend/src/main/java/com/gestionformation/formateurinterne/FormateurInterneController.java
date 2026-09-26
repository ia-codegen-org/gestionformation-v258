package com.gestionformation.formateurinterne;

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

@Tag(name = "FormateurInterne", description = "API de gestion des FormateurInterne")
@RestController
@RequestMapping("/api/formateurinterne")
@RequiredArgsConstructor
public class FormateurInterneController {

    private final FormateurInterneService service;

    private final SecurityUtils securityUtils;

    @Operation(
        summary = "Lister tous les FormateurInterne",
        description = "Retourne la liste complète des FormateurInterne"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('FormateurInterne', 'GET')")
    @GetMapping
    public ResponseEntity<List<FormateurInterneResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
        summary = "Compter les FormateurInterne",
        description = "Retourne le nombre total de FormateurInterne"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nombre retourné avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('FormateurInterne', 'GET')")
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @Operation(
        summary = "Trouver un FormateurInterne par ID",
        description = "Retourne un FormateurInterne selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "FormateurInterne trouvé"),
        @ApiResponse(responseCode = "404", description = "FormateurInterne non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('FormateurInterne', 'GET')")
    @GetMapping("/{id}")
    public ResponseEntity<FormateurInterneResponseDTO> findById(
            @Parameter(description = "ID du FormateurInterne") @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PreAuthorize("@permissionService.isAllowed('FormateurInterne', 'POST')")
    @PostMapping
    public ResponseEntity<FormateurInterneResponseDTO> create(
            @Valid @RequestBody FormateurInterneCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }


    @Operation(
        summary = "Mettre à jour un FormateurInterne",
        description = "Met à jour les informations d'un FormateurInterne existant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "FormateurInterne mis à jour avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "FormateurInterne non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('FormateurInterne', 'PUT')")
    @PutMapping("/{id}")
    public ResponseEntity<FormateurInterneResponseDTO> update(
            @Parameter(description = "ID du FormateurInterne") @PathVariable Long id,
            @Valid @RequestBody FormateurInterneUpdateDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
        summary = "Supprimer un FormateurInterne",
        description = "Supprime un FormateurInterne selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "FormateurInterne supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "FormateurInterne non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('FormateurInterne', 'DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du FormateurInterne") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
