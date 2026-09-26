# GestionFormation-frontend

Application Angular générée automatiquement par **IA-CodeGen**.

## 📋 Prérequis

- Node.js 18+
- npm

## 🚀 Guide de démarrage — Frontend Angular

1. **Accéder au dossier**
   ```bash
   cd gestionformation-frontend
   ```

2. **Installer les dépendances**
   ```bash
   npm install
   ```

3. **Installer Angular Material**
   ```bash
   npm install @angular/material @angular/cdk
   ```
   ⚠️ Obligatoire — requis pour les composants UI

4. **Lancer le serveur de développement**
   ```bash
   ng serve
   ```
   Ouvrir → http://localhost:4200

   Les outils de test (`karma`, `jasmine-core`...) sont déjà
   dans `package.json` (installés à l'étape 2) — `ng test`
   fonctionne directement, sans installation supplémentaire.

5. **S'assurer que le backend tourne**
   ```
   http://localhost:8080
   ```
   Le frontend communique avec le backend Spring Boot

6. **Lancer les tests unitaires** _(optionnel)_
   ```bash
   ng test
   ```
   Exécute les tests Jasmine/Karma une seule fois en headless
   (déjà configuré dans karma.conf.js — pas besoin d'options supplémentaires)


## ⚙️ Variables d'environnement

Angular n'utilise pas de fichier `.env` : la configuration se trouve dans `src/environments/`.

| Fichier                 | Utilisé pour                          | Variable à modifier                       |
|--------------------------|-----------------------------------------|---------------------------------------------|
| `environment.ts`         | mode développement (`npm start`)        | `apiUrl` (défaut `http://localhost:8080`)    |
| `environment.prod.ts`    | build de production (`npm run build`)   | `apiUrl` (défaut `https://api.monapp.com`)   |

Pour pointer vers un autre backend, éditez `apiUrl` dans le fichier correspondant :

```ts
export const environment = {
  production: false,
  apiUrl: 'http://localhost:9090'
};
```

## 🏗️ Build de production

```bash
npm run build
```
Les fichiers générés se trouvent dans `dist/`.

## 🔐 Authentification

Ce frontend intègre un module d'authentification (connexion, inscription, mot de passe oublié).
Le token JWT reçu du backend est stocké puis ajouté automatiquement aux requêtes HTTP
via l'intercepteur `auth.interceptor.ts`.

## 🗺️ Pages disponibles

| Route | Page | Accès |
|---|---|---|
| `/login` | Connexion | Public |
| `/forgot-password` | Mot de passe oublié | Public |
| `/reset-password` | Réinitialisation du mot de passe | Public |
| `/account-blocked` | Compte bloqué | Public |
| `/` | Accueil | Authentifié |
| `/dashboard` | Tableau de bord | Authentifié |
| `/profile` | Profil utilisateur | Authentifié |
| `/change-password` | Changer le mot de passe | Authentifié |
| `/formateur` | Liste Formateur | Authentifié |
| `/formateur/new` | Création Formateur | Authentifié |
| `/formateur/edit/:id` | Édition Formateur | Authentifié |
| `/formateur/view/:id` | Détail Formateur | Authentifié |
| `/formateurinterne` | Liste FormateurInterne | Authentifié |
| `/formateurinterne/new` | Création FormateurInterne | Authentifié |
| `/formateurinterne/edit/:id` | Édition FormateurInterne | Authentifié |
| `/formateurinterne/view/:id` | Détail FormateurInterne | Authentifié |
| `/formateurexterne` | Liste FormateurExterne | Authentifié |
| `/formateurexterne/new` | Création FormateurExterne | Authentifié |
| `/formateurexterne/edit/:id` | Édition FormateurExterne | Authentifié |
| `/formateurexterne/view/:id` | Détail FormateurExterne | Authentifié |
| `/etudiant` | Liste Etudiant | Authentifié |
| `/etudiant/new` | Création Etudiant | Authentifié |
| `/etudiant/edit/:id` | Édition Etudiant | Authentifié |
| `/etudiant/view/:id` | Détail Etudiant | Authentifié |
| `/formation` | Liste Formation | Authentifié |
| `/session` | Liste Session | Authentifié |
| `/salle` | Liste Salle | Authentifié |
| `/batiment` | Liste Batiment | Authentifié |
| `/departement` | Liste Departement | Authentifié |
| `/categorie` | Liste Categorie | Authentifié |
| `/inscription` | Liste Inscription | Authentifié |
| `/evaluation` | Liste Evaluation | Authentifié |
| `/certificat` | Liste Certificat | Authentifié |
| `/paiement` | Liste Paiement | Authentifié |
| `/facture` | Liste Facture | Authentifié |
| `/access-denied` | Accès refusé | Authentifié |
| `/**` | Page 404 | - |


## 🧩 Entités générées

Formateur, FormateurInterne, FormateurExterne, Etudiant, Formation, Session, Salle, Batiment, Departement, Categorie, Inscription, Evaluation, Certificat, Paiement, Facture

## 🧪 Tests

```bash
npm test
```
