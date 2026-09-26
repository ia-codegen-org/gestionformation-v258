package com.gestionformation.config;

import org.springframework.boot.context.event.ApplicationFailedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import java.sql.SQLException;

public class StartupErrorListener
        implements ApplicationListener<ApplicationFailedEvent>, PriorityOrdered {

    @Override
    public void onApplicationEvent(ApplicationFailedEvent event) {
        Throwable rootCause = findRootCause(event.getException());
        String message = rootCause.getMessage();
        String sqlState = extractSqlState(rootCause);

        if (message == null && sqlState == null) {
            return;
        }

        if (isAuthenticationFailure(sqlState, message)) {
            printBox(
                "❌ ÉCHEC DE CONNEXION POSTGRESQL — AUTHENTIFICATION REFUSÉE",
                "Le mot de passe utilisé pour se connecter à PostgreSQL est incorrect.",
                new String[] {
                    "Vérifie la variable SPRING_DATASOURCE_PASSWORD (ou DB_PASSWORD) dans ton .env",
                    "Vérifie que ce mot de passe correspond bien à celui du serveur PostgreSQL utilisé",
                    "Attention : si tu as DEUX PostgreSQL (un local + un dans Docker), ils peuvent avoir",
                    "  des mots de passe DIFFÉRENTS — configure celui du serveur réellement utilisé",
                    "Vérifie aussi spring.datasource.username / spring.datasource.url"
                }
            );
        } else if (isConnectionUnreachable(sqlState, message)) {
            printBox(
                "❌ ÉCHEC DE CONNEXION POSTGRESQL — SERVEUR INJOIGNABLE",
                "Impossible de joindre le serveur PostgreSQL (connexion refusée ou délai dépassé).",
                new String[] {
                    "Vérifie que PostgreSQL est bien démarré (service local ou conteneur Docker)",
                    "Si tu utilises Docker, vérifie que Docker Desktop est lancé et que le conteneur tourne",
                    "Vérifie que le PORT configuré (spring.datasource.url) correspond au port réellement exposé",
                    "  par le serveur PostgreSQL (ex : 5432 en local, éventuellement différent sous Docker)",
                    "Vérifie l'hôte (localhost, nom du service Docker...) utilisé dans l'URL de connexion"
                }
            );
        }
        // Toute autre erreur non reconnue : Spring Boot affiche son
        // log complet habituel — on ne masque rien d'inattendu.
    }

    private boolean isAuthenticationFailure(String sqlState, String message) {
        if (sqlState != null && sqlState.startsWith("28")) {
            return true;
        }
        if (message == null) {
            return false;
        }
        String lower = message.toLowerCase();
        return lower.contains("password authentication failed")
                || lower.contains("authentication failed");
    }

    private boolean isConnectionUnreachable(String sqlState, String message) {
        if (sqlState != null && sqlState.startsWith("08")) {
            return true;
        }
        if (message == null) {
            return false;
        }
        String lower = message.toLowerCase();
        return lower.contains("connection refused")
                || lower.contains("the connection attempt failed")
                || lower.contains("timeout")
                || lower.contains("could not connect to server");
    }

    private String extractSqlState(Throwable throwable) {
        if (throwable instanceof SQLException sqlException) {
            return sqlException.getSQLState();
        }
        return null;
    }

    private Throwable findRootCause(Throwable throwable) {
        Throwable current = throwable;
        while (current.getCause() != null && current.getCause() != current) {
            current = current.getCause();
        }
        return current;
    }

    private void printBox(String title, String description, String[] actions) {
        String separator = "=".repeat(80);

        StringBuilder sb = new StringBuilder();
        sb.append('\n').append(separator).append('\n');
        sb.append(title).append('\n');
        sb.append(separator).append('\n');
        sb.append(description).append('\n');
        sb.append('\n');
        sb.append("Actions à vérifier :\n");
        for (String action : actions) {
            sb.append("  → ").append(action).append('\n');
        }
        sb.append(separator).append('\n');

        System.err.println(sb);
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
