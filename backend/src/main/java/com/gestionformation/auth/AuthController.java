package com.gestionformation.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.gestionformation.user.UserService;
import com.gestionformation.user.User;
import com.gestionformation.user.UserRepository;
import com.gestionformation.user.UserResponseDTO;
import com.gestionformation.exception.ResourceNotFoundException;

@Tag(name = "Auth", description = "API d'authentification")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService    authService;
    private final UserService    userService;
    private final UserRepository userRepository;

    @Operation(summary = "Inscription",
               description = "Crée un compte utilisateur avec rôle USER")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inscription réussie"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(summary = "Connexion",
               description = "Authentifie et retourne un JWT")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Connexion réussie"),
        @ApiResponse(responseCode = "401", description = "Identifiants incorrects")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PatchMapping("/users/{id}/block")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Bloquer un compte utilisateur")
    public ResponseEntity<Void> blockUser(@PathVariable Long id) {
        userService.setEnabled(id, false);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/users/{id}/unblock")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Débloquer un compte utilisateur")
    public ResponseEntity<Void> unblockUser(@PathVariable Long id) {
        userService.setEnabled(id, true);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtenir le profil d'un utilisateur")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Profil retourné"),
        @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PreAuthorize("hasRole('ADMIN') or authentication.principal.id == #id")
    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(
            @PathVariable Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(
                "User not found with id: " + id));
        return ResponseEntity.ok(UserResponseDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .role(user.getRole().name())
            .enabled(user.isEnabled())
            .build());
    }

    @Operation(summary = "Changer le mot de passe")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mot de passe changé"),
        @ApiResponse(responseCode = "400", description = "Ancien mot de passe incorrect")
    })
    @PatchMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            java.security.Principal principal) {
        authService.changePassword(principal.getName(), request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Demander une réinitialisation de mot de passe",
               description = "Envoie un email avec un lien de réinitialisation")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Email envoyé si le compte existe")
    })
    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request,
            jakarta.servlet.http.HttpServletRequest httpRequest) {
        String origin = httpRequest.getHeader("Origin");
        authService.forgotPassword(request, origin);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Réinitialiser le mot de passe",
               description = "Change le mot de passe à partir d'un token reçu par email")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Mot de passe réinitialisé"),
        @ApiResponse(responseCode = "400", description = "Token invalide ou expiré")
    })
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ResponseEntity.ok().build();
    }
}
