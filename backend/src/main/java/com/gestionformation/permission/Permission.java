package com.gestionformation.permission;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions", uniqueConstraints = @UniqueConstraint(
        columnNames = {"role", "entity_name", "action"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String role;

    @Column(name = "entity_name", nullable = false)
    private String entityName;

    // ✅ GET / POST / PUT / DELETE
    @Column(nullable = false)
    private String action;

    @Column(nullable = false)
    private boolean allowed;

    // ✅ 'OWN' | 'ALL' | null — calculé à la génération depuis
    // EntityItem.readScope, informatif uniquement (non modifiable via
    // l'API : la restriction réelle est câblée en dur dans le repository/
    // service générés, voir ArchitectureGeneratorService.buildActorAccessCheck).
    @Column(name = "read_scope")
    private String readScope;
}
