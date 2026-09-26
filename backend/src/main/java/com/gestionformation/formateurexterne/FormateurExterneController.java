package com.gestionformation.formateurexterne;

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

@Tag(name = "FormateurExterne", description = "API de gestion des FormateurExterne")
@RestController
@RequestMapping("/api/formateurexterne")
@RequiredArgsConstructor
public class FormateurExterneController {

    private final FormateurExterneService service;

    private final SecurityUtils securityUtils;

    @Operation(
        summary = "Lister tous les FormateurExterne",
        description = "Retourne la liste complète des FormateurExterne"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('FormateurExterne', 'GET')")
    @GetMapping
    public ResponseEntity<List<FormateurExterneResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
        summary = "Compter les FormateurExterne",
        description = "Retourne le nombre total de FormateurExterne"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Nombre retourné avec succès")
    })
    @PreAuthorize("@permissionService.isAllowed('FormateurExterne', 'GET')")
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @Operation(
        summary = "Trouver un FormateurExterne par ID",
        description = "Retourne un FormateurExterne selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "FormateurExterne trouvé"),
        @ApiResponse(responseCode = "404", description = "FormateurExterne non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('FormateurExterne', 'GET')")
    @GetMapping("/{id}")
    public ResponseEntity<FormateurExterneResponseDTO> findById(
            @Parameter(description = "ID du FormateurExterne") @PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PreAuthorize("@permissionService.isAllowed('FormateurExterne', 'POST')")
    @PostMapping
    public ResponseEntity<FormateurExterneResponseDTO> create(
            @Valid @RequestBody FormateurExterneCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(dto));
    }


    @Operation(
        summary = "Mettre à jour un FormateurExterne",
        description = "Met à jour les informations d'un FormateurExterne existant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "FormateurExterne mis à jour avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "404", description = "FormateurExterne non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('FormateurExterne', 'PUT')")
    @PutMapping("/{id}")
    public ResponseEntity<FormateurExterneResponseDTO> update(
            @Parameter(description = "ID du FormateurExterne") @PathVariable Long id,
            @Valid @RequestBody FormateurExterneUpdateDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(
        summary = "Supprimer un FormateurExterne",
        description = "Supprime un FormateurExterne selon son identifiant"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "FormateurExterne supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "FormateurExterne non trouvé")
    })
    @PreAuthorize("@permissionService.isAllowed('FormateurExterne', 'DELETE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID du FormateurExterne") @PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
