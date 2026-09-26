package com.gestionformation.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionResponseDTO {

    @Schema(description = "Identifiant de la permission")
    private Long id;

    @Schema(description = "Rôle concerné", example = "ADMIN")
    private String role;

    @Schema(description = "Entité concernée", example = "Formation")
    private String entityName;

    @Schema(description = "Action HTTP (GET/POST/PUT/DELETE)", example = "GET")
    private String action;

    @Schema(description = "Autorisé ou non")
    private boolean allowed;

    @Schema(description = "Portée de lecture pour ce rôle sur cette entité ('OWN' = uniquement ses propres données, 'ALL' = toutes), absente si non applicable", example = "OWN")
    private String readScope;
}
