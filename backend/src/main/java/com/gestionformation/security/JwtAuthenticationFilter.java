package com.gestionformation.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import io.jsonwebtoken.JwtException;
import com.gestionformation.exception.ApiError;
import com.gestionformation.user.User;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil                jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper =
            new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.substring(7);

        try {
            final String username = jwtUtil.extractUsername(jwt);

        if (username != null
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.isTokenValid(jwt, userDetails)) {

                User user = (User) userDetails;

                // ─── Compte bloqué par admin pendant la session ─────
                if (!user.isEnabled()) {
                    ApiError error = new ApiError(
                            LocalDateTime.now(), 403, "Forbidden",
                            "ACCOUNT_BLOCKED",
                            "Votre compte a été bloqué. Contactez l'administrateur.",
                            request.getRequestURI());
                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    objectMapper.findAndRegisterModules();
                    objectMapper.writeValue(response.getOutputStream(), error);
                    return;
                }

                // ─── Mot de passe temporaire non changé ─────────────
                if (user.isMustChangePassword()
                        && !request.getRequestURI().equals("/auth/change-password")) {

                    ApiError error = new ApiError(
                            LocalDateTime.now(), 403, "Forbidden",
                            "MUST_CHANGE_PASSWORD",
                            "Vous devez changer votre mot de passe avant de continuer",
                            request.getRequestURI());

                    response.setStatus(HttpStatus.FORBIDDEN.value());
                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    objectMapper.findAndRegisterModules();
                    objectMapper.writeValue(response.getOutputStream(), error);
                    return;
                }

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        } catch (JwtException e) {
            ApiError error = new ApiError(
                    LocalDateTime.now(), 401, "Unauthorized",
                    "INVALID_TOKEN",
                    "Session expirée ou token invalide, veuillez vous reconnecter",
                    request.getRequestURI());
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            objectMapper.findAndRegisterModules();
            objectMapper.writeValue(response.getOutputStream(), error);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
