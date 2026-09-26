package com.gestionformation.permission;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Permissions", description = "Gestion des permissions (réservé aux administrateurs)")
@RestController
@RequestMapping("/api/admin/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    // ✅ Accessible à TOUT utilisateur connecté (pas ADMIN seulement) —
    // sert au frontend (sidebar + guards) à rafraîchir ses propres droits
    // à chaque connexion, sans redéployer l'app si l'admin a changé la
    // matrice entre-temps.
    @Operation(summary = "Mes permissions (rôle actuellement connecté)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Permissions retournées avec succès")
    })
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/mine")
    public ResponseEntity<Map<String, Map<String, Boolean>>> findMine() {
        return ResponseEntity.ok(permissionService.findForCurrentUser());
    }

    @Operation(summary = "Lister toutes les permissions")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Liste retournée avec succès")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PermissionResponseDTO>> findAll() {
        return ResponseEntity.ok(permissionService.findAll());
    }

    @Operation(summary = "Modifier une permission (autoriser/refuser)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Permission mise à jour"),
        @ApiResponse(responseCode = "404", description = "Permission introuvable")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PermissionResponseDTO> updateAllowed(
            @PathVariable Long id,
            @Valid @RequestBody PermissionUpdateRequest request) {
        return ResponseEntity.ok(
                permissionService.updateAllowed(id, request.getAllowed()));
    }
}
