package com.gestionformation.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.dao.InvalidDataAccessResourceUsageException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(buildError(404, "Not Found",
                "RESOURCE_NOT_FOUND", ex.getMessage(),
                request.getRequestURI()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
            .forEach(err -> fieldErrors.put(
                err.getField(), err.getDefaultMessage()));
        ApiError error = buildError(400, "Bad Request",
            "VALIDATION_ERROR",
            "Un ou plusieurs champs sont invalides",
            request.getRequestURI());
        error.setErrors(fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadable(
            HttpMessageNotReadableException ex,
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(buildError(400, "Bad Request",
                "INVALID_JSON",
                "Le corps de la requete est invalide ou contient des champs inconnus",
                request.getRequestURI()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(
            DataIntegrityViolationException ex,
            HttpServletRequest request) {
        Throwable cause = ex.getMostSpecificCause();
        String sqlState = (cause instanceof java.sql.SQLException sqlEx)
                ? sqlEx.getSQLState() : null;
        String causeMessage = (cause != null && cause.getMessage() != null)
                ? cause.getMessage().toLowerCase() : "";

        // Code SQL standard 23514 = violation de contrainte CHECK
        // (ex: valeur d enum non autorisee) — n a rien a voir avec un doublon
        boolean isCheckViolation = "23514".equals(sqlState)
                || causeMessage.contains("check constraint")
                || causeMessage.contains("contrainte de verification")
                || causeMessage.contains("contrainte de v\u00e9rification");
        if (isCheckViolation) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(400, "Bad Request",
                    "INVALID_VALUE",
                    "La valeur fournie n est pas autorisee pour ce champ",
                    request.getRequestURI()));
        }

        String message = "Cette valeur existe deja";
        if      (causeMessage.contains("email"))     message = "Cet email est deja utilise";
        else if (causeMessage.contains("username"))  message = "Ce nom d utilisateur est deja utilise";
        else if (causeMessage.contains("telephone")) message = "Ce telephone est deja utilise";
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(buildError(409, "Conflict",
                "DUPLICATE_ENTRY", message,
                request.getRequestURI()));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ApiError> handleDuplicateResource(
            DuplicateResourceException ex,
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(buildError(409, "Conflict",
                "DUPLICATE_ENTRY", ex.getMessage(),
                request.getRequestURI()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(
            IllegalArgumentException ex,
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(buildError(400, "Bad Request",
                "INVALID_ARGUMENT", ex.getMessage(),
                request.getRequestURI()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDenied(
            AccessDeniedException ex,
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(buildError(403, "Forbidden",
                "ACCESS_DENIED",
                "Acces refuse — droits insuffisants",
                request.getRequestURI()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(
            BadCredentialsException ex,
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(buildError(401, "Unauthorized",
                "BAD_CREDENTIALS",
                "Identifiants incorrects",
                request.getRequestURI()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiError> handleUsernameNotFound(
            UsernameNotFoundException ex,
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(buildError(401, "Unauthorized",
                "BAD_CREDENTIALS",
                "Identifiants incorrects",
                request.getRequestURI()));
    }

    @ExceptionHandler(AccountBlockedException.class)
    public ResponseEntity<ApiError> handleAccountBlocked(
            AccountBlockedException ex,
            HttpServletRequest request) {
        ApiError error = new ApiError(
                LocalDateTime.now(),
                403,
                "Forbidden",
                "ACCOUNT_BLOCKED",
                ex.getMessage(),
                request.getRequestURI());
        return ResponseEntity.status(403).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneral(
            Exception ex,
            HttpServletRequest request) {
        log.error("Erreur interne non gérée", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(buildError(500, "Internal Server Error",
                "INTERNAL_ERROR",
                ex.getClass().getSimpleName() + " : " + ex.getMessage(),
                request.getRequestURI()));
    }

    private ApiError buildError(int status, String error,
            String errorCode, String message, String path) {
        return new ApiError(
                LocalDateTime.now(), status,
                error, errorCode, message, path);
    }
    @ExceptionHandler(org.springframework.dao.InvalidDataAccessResourceUsageException.class)
    public ResponseEntity<ApiError> handleInvalidDataAccess(
            org.springframework.dao.InvalidDataAccessResourceUsageException ex,
            HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(buildError(500, "Internal Server Error",
                "DATA_INTEGRITY_ERROR",
                "Une donnée en base est incompatible avec le modèle actuel.",
                request.getRequestURI()));
    }

}
