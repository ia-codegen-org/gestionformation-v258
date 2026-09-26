package com.gestionformation.permission;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByRoleAndEntityNameAndAction(
            String role, String entityName, String action);

    List<Permission> findByEntityNameOrderByRoleAscActionAsc(String entityName);
}
