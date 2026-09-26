package com.gestionformation.permission;

import com.gestionformation.exception.ResourceNotFoundException;
import com.gestionformation.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final SecurityUtils        securityUtils;

    // ✅ Appelé depuis les annotations @PreAuthorize générées sur les
    // contrôleurs (@permissionService.isAllowed('Entite','GET')) — la
    // matrice est éditable à chaud par l'admin, plus figée dans le code
    // compilé.
    public boolean isAllowed(String entityName, String action) {
        String role;
        try {
            role = securityUtils.getConnectedRole();
        } catch (Exception e) {
            return false;
        }
        if (role == null || role.isBlank()) {
            return false;
        }
        // ✅ Garde-fou — ADMIN garde toujours accès, pour éviter qu'un admin
        // se verrouille lui-même hors de l'application en modifiant la
        // matrice par erreur.
        if ("ADMIN".equals(role)) {
            return true;
        }
        return permissionRepository
                .findByRoleAndEntityNameAndAction(role, entityName, action)
                .map(Permission::isAllowed)
                .orElse(false);
    }

    // ✅ Appelé par le frontend (sidebar + guards) à chaque connexion —
    // permet à l'app de refléter les changements faits par l'admin
    // dans "Gestion des permissions" sans reconstruire le frontend.
    public Map<String, Map<String, Boolean>> findForCurrentUser() {
        Map<String, Map<String, Boolean>> result = new HashMap<>();
        String role;
        try {
            role = securityUtils.getConnectedRole();
        } catch (Exception e) {
            return result;
        }
        if (role == null || role.isBlank()) {
            return result;
        }
        for (Permission permission : permissionRepository.findAll()) {
            if (!role.equals(permission.getRole())) continue;
            result.computeIfAbsent(permission.getEntityName(), k -> new HashMap<>())
                  .put(permission.getAction(), permission.isAllowed());
        }
        return result;
    }

    public List<PermissionResponseDTO> findAll() {
        return permissionRepository.findAll().stream()
                .map(this::toDTO)
                .toList();
    }

    public PermissionResponseDTO updateAllowed(Long id, boolean allowed) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Permission " + id + " introuvable"));
        // ✅ Garde-fou serveur — ADMIN garde toujours un accès complet et
        // fixe : rejeté même si la requête est envoyée directement (pas
        // seulement caché côté frontend), pour éviter qu'un admin se
        // verrouille lui-même hors de l'application.
        if ("ADMIN".equals(permission.getRole())) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "Les permissions du rôle ADMIN ne sont pas modifiables — "
                    + "ADMIN dispose toujours d'un accès complet.");
        }
        permission.setAllowed(allowed);
        return toDTO(permissionRepository.save(permission));
    }

    private PermissionResponseDTO toDTO(Permission permission) {
        return PermissionResponseDTO.builder()
                .id(permission.getId())
                .role(permission.getRole())
                .entityName(permission.getEntityName())
                .action(permission.getAction())
                .allowed(permission.isAllowed())
                .readScope(permission.getReadScope())
                .build();
    }
}
