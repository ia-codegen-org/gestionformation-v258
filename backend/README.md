# GestionFormation

Backend Spring Boot généré automatiquement par **IA-CodeGen**.

## 📋 Prérequis

- Java 17+
- PostgreSQL en cours d'exécution (aucune installation de Maven requise, le wrapper `mvnw` est fourni)

## 🚀 Démarrage rapide

1. Créer la base de données PostgreSQL :
   ```sql
   CREATE DATABASE gestionformation;
   ```
2. Lancer l'application :
   ```bash
   ./mvnw spring-boot:run        # Linux / macOS
   mvnw.cmd spring-boot:run      # Windows
   ```
3. API disponible sur : http://localhost:8080
4. Documentation Swagger : http://localhost:8080/swagger-ui/index.html

## ⚙️ Variables d'environnement

La configuration se trouve dans `src/main/resources/application.properties`.
Chaque valeur `${VARIABLE:defaut}` peut être surchargée par une variable d'environnement
système, sans modifier le fichier :

| Variable        | Description                             | Valeur par défaut          |
|-----------------|-------------------------------------------|------------------------------|
| `DB_PASSWORD`   | Mot de passe PostgreSQL                    | `changeme`                    |
| `MAIL_USERNAME` | Identifiant SMTP (envoi d'emails)          | `changeme@gmail.com`          |
| `MAIL_PASSWORD` | Mot de passe SMTP                          | `changeme`                    |
| `MAIL_FROM`     | Adresse d'expédition des emails            | `noreply@gestionformation.com`              |
| `FRONTEND_URL`  | URL du frontend (liens dans les emails)    | `http://localhost:4200`       |

Exemple pour changer une variable sans toucher au code :

```bash
# Linux / macOS
export DB_PASSWORD=monMotDePasse
./mvnw spring-boot:run

# Windows (PowerShell)
$env:DB_PASSWORD = "monMotDePasse"
.\mvnw.cmd spring-boot:run
```

Vous pouvez aussi modifier directement la valeur par défaut dans `application.properties`
(partie après le `:` dans `${VARIABLE:defaut}`).

Autres réglages utiles dans `application.properties` :
- `server.port` — port HTTP de l'application (défaut `8080`)
- `spring.datasource.url` — URL de connexion PostgreSQL
- `spring.jpa.hibernate.ddl-auto` — stratégie de synchronisation du schéma (`update` par défaut)

## 🔐 Authentification

La sécurité est activée sur ce projet (JWT).

- Inscription : `POST /api/auth/register`
- Connexion : `POST /api/auth/login`
- Envoyer le token reçu dans l'en-tête `Authorization: Bearer <token>` pour accéder aux routes protégées.

## 🧩 Entités générées

Formateur, FormateurInterne, FormateurExterne, Etudiant, Formation, Session, Salle, Batiment, Departement, Categorie, Inscription, Evaluation, Certificat, Paiement, Facture

## 📡 Endpoints générés

Documentation interactive (tester les requêtes) : http://localhost:8080/swagger-ui/index.html

### Authentification

| Méthode | Endpoint | Description |
|---|---|---|
| POST | `/auth/register` | Inscription |
| POST | `/auth/login` | Connexion — retourne le token JWT |
| GET | `/auth/users/{id}` | Détail d'un utilisateur |
| PATCH | `/auth/users/{id}/block` | Bloquer un utilisateur |
| PATCH | `/auth/users/{id}/unblock` | Débloquer un utilisateur |
| PATCH | `/auth/change-password` | Changer son mot de passe |
| POST | `/auth/forgot-password` | Demander une réinitialisation |
| POST | `/auth/reset-password` | Réinitialiser le mot de passe |
| GET | `/api/users` | Liste globale des utilisateurs (lecture seule) |
| GET | `/api/users/{id}` | Détail d'un utilisateur |

### Par entité

**Formateur** — `/api/formateur`

| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/formateur` | Lister |
| GET | `/api/formateur/count` | Compter |
| GET | `/api/formateur/{id}` | Détail |
| POST | `/api/formateur` | Créer |
| PUT | `/api/formateur/{id}` | Modifier |
| DELETE | `/api/formateur/{id}` | Supprimer |

**FormateurInterne** — `/api/formateurinterne`

| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/formateurinterne` | Lister |
| GET | `/api/formateurinterne/count` | Compter |
| GET | `/api/formateurinterne/{id}` | Détail |
| POST | `/api/formateurinterne` | Créer |
| PUT | `/api/formateurinterne/{id}` | Modifier |
| DELETE | `/api/formateurinterne/{id}` | Supprimer |

**FormateurExterne** — `/api/formateurexterne`

| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/formateurexterne` | Lister |
| GET | `/api/formateurexterne/count` | Compter |
| GET | `/api/formateurexterne/{id}` | Détail |
| POST | `/api/formateurexterne` | Créer |
| PUT | `/api/formateurexterne/{id}` | Modifier |
| DELETE | `/api/formateurexterne/{id}` | Supprimer |

**Etudiant** — `/api/etudiant`

| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/etudiant` | Lister |
| GET | `/api/etudiant/count` | Compter |
| GET | `/api/etudiant/{id}` | Détail |
| POST | `/api/etudiant` | Créer |
| PUT | `/api/etudiant/{id}` | Modifier |
| DELETE | `/api/etudiant/{id}` | Supprimer |

**Formation** — `/api/formation`

| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/formation` | Lister |
| GET | `/api/formation/count` | Compter |
| GET | `/api/formation/{id}` | Détail |
| POST | `/api/formation` | Créer |
| PUT | `/api/formation/{id}` | Modifier |
| DELETE | `/api/formation/{id}` | Supprimer |

**Session** — `/api/session`

| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/session` | Lister |
| GET | `/api/session/count` | Compter |
| GET | `/api/session/{id}` | Détail |
| POST | `/api/session` | Créer |
| PUT | `/api/session/{id}` | Modifier |
| DELETE | `/api/session/{id}` | Supprimer |

**Salle** — `/api/salle`

| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/salle` | Lister |
| GET | `/api/salle/count` | Compter |
| GET | `/api/salle/{id}` | Détail |
| POST | `/api/salle` | Créer |
| PUT | `/api/salle/{id}` | Modifier |
| DELETE | `/api/salle/{id}` | Supprimer |

**Batiment** — `/api/batiment`

| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/batiment` | Lister |
| GET | `/api/batiment/count` | Compter |
| GET | `/api/batiment/{id}` | Détail |
| POST | `/api/batiment` | Créer |
| PUT | `/api/batiment/{id}` | Modifier |
| DELETE | `/api/batiment/{id}` | Supprimer |

**Departement** — `/api/departement`

| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/departement` | Lister |
| GET | `/api/departement/count` | Compter |
| GET | `/api/departement/{id}` | Détail |
| POST | `/api/departement` | Créer |
| PUT | `/api/departement/{id}` | Modifier |
| DELETE | `/api/departement/{id}` | Supprimer |

**Categorie** — `/api/categorie`

| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/categorie` | Lister |
| GET | `/api/categorie/count` | Compter |
| GET | `/api/categorie/{id}` | Détail |
| POST | `/api/categorie` | Créer |
| PUT | `/api/categorie/{id}` | Modifier |
| DELETE | `/api/categorie/{id}` | Supprimer |

**Inscription** — `/api/inscription`

| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/inscription` | Lister |
| GET | `/api/inscription/count` | Compter |
| GET | `/api/inscription/{id}` | Détail |
| POST | `/api/inscription` | Créer |
| PUT | `/api/inscription/{id}` | Modifier |
| DELETE | `/api/inscription/{id}` | Supprimer |

**Evaluation** — `/api/evaluation`

| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/evaluation` | Lister |
| GET | `/api/evaluation/count` | Compter |
| GET | `/api/evaluation/{id}` | Détail |
| POST | `/api/evaluation` | Créer |
| PUT | `/api/evaluation/{id}` | Modifier |
| DELETE | `/api/evaluation/{id}` | Supprimer |

**Certificat** — `/api/certificat`

| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/certificat` | Lister |
| GET | `/api/certificat/count` | Compter |
| GET | `/api/certificat/{id}` | Détail |
| POST | `/api/certificat` | Créer |
| PUT | `/api/certificat/{id}` | Modifier |
| DELETE | `/api/certificat/{id}` | Supprimer |

**Paiement** — `/api/paiement`

| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/paiement` | Lister |
| GET | `/api/paiement/count` | Compter |
| GET | `/api/paiement/{id}` | Détail |
| POST | `/api/paiement` | Créer |
| PUT | `/api/paiement/{id}` | Modifier |
| DELETE | `/api/paiement/{id}` | Supprimer |

**Facture** — `/api/facture`

| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/api/facture` | Lister |
| GET | `/api/facture/count` | Compter |
| GET | `/api/facture/{id}` | Détail |
| POST | `/api/facture` | Créer |
| PUT | `/api/facture/{id}` | Modifier |
| DELETE | `/api/facture/{id}` | Supprimer |

> Des endpoints supplémentaires peuvent exister selon les relations entre entités (ex: navigation vers une entité liée) — voir Swagger pour la liste exhaustive.

## 🧪 Tests

```bash
./mvnw test
```
