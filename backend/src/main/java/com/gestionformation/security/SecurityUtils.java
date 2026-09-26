package com.gestionformation.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.gestionformation.user.User;
import com.gestionformation.exception.ResourceNotFoundException;

@Component
public class SecurityUtils {

    public User getConnectedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()
                || !(auth.getPrincipal() instanceof User)) {
            throw new ResourceNotFoundException("Aucun utilisateur connecté");
        }
        return (User) auth.getPrincipal();
    }

    public Long getConnectedUserId() {
        return getConnectedUser().getId();
    }

    public String getConnectedUsername() {
        return getConnectedUser().getUsername();
    }

    public boolean hasRole(String role) {
        return getConnectedUser().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }

    public String getCurrentRole() {
        return getConnectedUser().getRole().name();
    }

    public String getConnectedRole() {
        return org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication()
                .getAuthorities().stream()
                .findFirst()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .orElse("");
    }

    public boolean isAdmin() {
        return "ADMIN".equals(getCurrentRole());
    }

    public boolean isOwner(String createdBy) {
        if (isAdmin()) return true;
        String currentUsername = getConnectedUsername();
        return currentUsername != null && currentUsername.equals(createdBy);
    }

    public boolean isAdminSafe() {
        try {
            return isAdmin();
        } catch (Exception e) {
            return false;
        }
    }
}
