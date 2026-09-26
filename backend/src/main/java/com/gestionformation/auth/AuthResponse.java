package com.gestionformation.auth;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Réponse d'authentification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

    @Schema(description = "ID utilisateur")
    private Long id;

    @Schema(description = "Token JWT")
    private String token;

    @Schema(description = "Type de token", example = "Bearer")
    private String type = "Bearer";

    @Schema(description = "Username")
    private String username;

    @Schema(description = "Rôle")
    private String role;

    @Schema(description = "Doit changer le password")
    private boolean mustChangePassword;

    public AuthResponse(Long id,String token, String username,
                        String role, boolean mustChangePassword) {
        this.id                 = id;
        this.token              = token;
        this.type               = "Bearer";
        this.username           = username;
        this.role               = role;
        this.mustChangePassword = mustChangePassword;
    }
}
