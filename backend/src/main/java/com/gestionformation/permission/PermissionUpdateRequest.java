package com.gestionformation.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionUpdateRequest {

    @NotNull(message = "allowed ne peut pas être null")
    @Schema(description = "Nouvelle valeur autorisée/refusée", example = "true")
    private Boolean allowed;
}
