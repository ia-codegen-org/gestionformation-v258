import { Routes }          from '@angular/router';
import { authGuard }        from './core/guards/auth.guard';
import { permissionGuard }  from './core/guards/permission.guard';
import { adminGuard }       from './core/guards/admin.guard';

export const routes: Routes = [

  // ─── Publiques
  {
    path:          'login',
    loadComponent: () =>
      import('./features/auth/login/login.component')
        .then(m => m.LoginComponent)
  },

      {
        path: 'forgot-password',
        loadComponent: () => import('./features/auth/forgot-password/forgot-password.component')
            .then(m => m.ForgotPasswordComponent)
      },
      {
        path: 'reset-password',
        loadComponent: () => import('./features/auth/reset-password/reset-password.component')
            .then(m => m.ResetPasswordComponent)
      },

  {
    path:          'account-blocked',
    loadComponent: () =>
      import('./features/auth/blocked-account/blocked-account.component')
        .then(m => m.BlockedAccountComponent)
  },

  // ─── Privées
  {
    path:        '',
    canActivate: [authGuard],
    children: [

      {
        path:          'change-password',
        loadComponent: () =>
          import('./features/auth/change-password/change-password.component')
            .then(m => m.ChangePasswordComponent)
      },

      {
        path:          'profile',
        loadComponent: () =>
          import('./features/profile/profile.component')
            .then(m => m.ProfileComponent)
      },

      {
        path:          'dashboard',
        loadComponent: () =>
          import('./features/dashboard/dashboard.component')
            .then(m => m.DashboardComponent)
      },

      {
        path:          '',
        loadComponent: () =>
          import('./features/home/home.component')
            .then(m => m.HomeComponent)
      },

      {
        path:          'access-denied',
        loadComponent: () =>
          import('./features/access-denied/access-denied.component')
            .then(m => m.AccessDeniedComponent)
      },

      {
        path:          'admin/permissions',
        loadComponent: () =>
          import('./features/admin/permission-management/permission-management.component')
            .then(m => m.PermissionManagementComponent),
        canActivate:   [adminGuard]
      },

      {
        path:          'formateur',
        loadComponent: () =>
          import('./features/formateur/formateur-list/formateur-list.component').then(m => m.FormateurListComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'Formateur', method: 'GET' }
      },
      {
        path:          'formateur/new',
        loadComponent: () =>
          import('./features/formateur/formateur-form/formateur-form.component').then(m => m.FormateurFormComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'Formateur', method: 'GET' }
      },
      {
        path:          'formateur/edit/:id',
        loadComponent: () =>
          import('./features/formateur/formateur-form/formateur-form.component').then(m => m.FormateurFormComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'Formateur', method: 'GET' }
      },
      {
        path:          'formateur/view/:id',
        loadComponent: () =>
          import('./features/formateur/formateur-form/formateur-form.component').then(m => m.FormateurFormComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'Formateur', method: 'GET' }
      },
      {
        path:          'formateurinterne',
        loadComponent: () =>
          import('./features/formateurinterne/formateurinterne-list/formateurinterne-list.component').then(m => m.FormateurInterneListComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'FormateurInterne', method: 'GET' }
      },
      {
        path:          'formateurinterne/new',
        loadComponent: () =>
          import('./features/formateurinterne/formateurinterne-form/formateurinterne-form.component').then(m => m.FormateurInterneFormComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'FormateurInterne', method: 'GET' }
      },
      {
        path:          'formateurinterne/edit/:id',
        loadComponent: () =>
          import('./features/formateurinterne/formateurinterne-form/formateurinterne-form.component').then(m => m.FormateurInterneFormComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'FormateurInterne', method: 'GET' }
      },
      {
        path:          'formateurinterne/view/:id',
        loadComponent: () =>
          import('./features/formateurinterne/formateurinterne-form/formateurinterne-form.component').then(m => m.FormateurInterneFormComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'FormateurInterne', method: 'GET' }
      },
      {
        path:          'formateurexterne',
        loadComponent: () =>
          import('./features/formateurexterne/formateurexterne-list/formateurexterne-list.component').then(m => m.FormateurExterneListComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'FormateurExterne', method: 'GET' }
      },
      {
        path:          'formateurexterne/new',
        loadComponent: () =>
          import('./features/formateurexterne/formateurexterne-form/formateurexterne-form.component').then(m => m.FormateurExterneFormComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'FormateurExterne', method: 'GET' }
      },
      {
        path:          'formateurexterne/edit/:id',
        loadComponent: () =>
          import('./features/formateurexterne/formateurexterne-form/formateurexterne-form.component').then(m => m.FormateurExterneFormComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'FormateurExterne', method: 'GET' }
      },
      {
        path:          'formateurexterne/view/:id',
        loadComponent: () =>
          import('./features/formateurexterne/formateurexterne-form/formateurexterne-form.component').then(m => m.FormateurExterneFormComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'FormateurExterne', method: 'GET' }
      },
      {
        path:          'etudiant',
        loadComponent: () =>
          import('./features/etudiant/etudiant-list/etudiant-list.component').then(m => m.EtudiantListComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'Etudiant', method: 'GET' }
      },
      {
        path:          'etudiant/new',
        loadComponent: () =>
          import('./features/etudiant/etudiant-form/etudiant-form.component').then(m => m.EtudiantFormComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'Etudiant', method: 'GET' }
      },
      {
        path:          'etudiant/edit/:id',
        loadComponent: () =>
          import('./features/etudiant/etudiant-form/etudiant-form.component').then(m => m.EtudiantFormComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'Etudiant', method: 'GET' }
      },
      {
        path:          'etudiant/view/:id',
        loadComponent: () =>
          import('./features/etudiant/etudiant-form/etudiant-form.component').then(m => m.EtudiantFormComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'Etudiant', method: 'GET' }
      },
      {
        path:          'formation',
        loadComponent: () =>
          import('./features/formation/formation-list/formation-list.component').then(m => m.FormationListComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'Formation', method: 'GET' }
      },
      {
        path:          'session',
        loadComponent: () =>
          import('./features/session/session-list/session-list.component').then(m => m.SessionListComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'Session', method: 'GET' }
      },
      {
        path:          'salle',
        loadComponent: () =>
          import('./features/salle/salle-list/salle-list.component').then(m => m.SalleListComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'Salle', method: 'GET' }
      },
      {
        path:          'batiment',
        loadComponent: () =>
          import('./features/batiment/batiment-list/batiment-list.component').then(m => m.BatimentListComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'Batiment', method: 'GET' }
      },
      {
        path:          'departement',
        loadComponent: () =>
          import('./features/departement/departement-list/departement-list.component').then(m => m.DepartementListComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'Departement', method: 'GET' }
      },
      {
        path:          'categorie',
        loadComponent: () =>
          import('./features/categorie/categorie-list/categorie-list.component').then(m => m.CategorieListComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'Categorie', method: 'GET' }
      },
      {
        path:          'inscription',
        loadComponent: () =>
          import('./features/inscription/inscription-list/inscription-list.component').then(m => m.InscriptionListComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'Inscription', method: 'GET' }
      },
      {
        path:          'evaluation',
        loadComponent: () =>
          import('./features/evaluation/evaluation-list/evaluation-list.component').then(m => m.EvaluationListComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'Evaluation', method: 'GET' }
      },
      {
        path:          'certificat',
        loadComponent: () =>
          import('./features/certificat/certificat-list/certificat-list.component').then(m => m.CertificatListComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'Certificat', method: 'GET' }
      },
      {
        path:          'paiement',
        loadComponent: () =>
          import('./features/paiement/paiement-list/paiement-list.component').then(m => m.PaiementListComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'Paiement', method: 'GET' }
      },
      {
        path:          'facture',
        loadComponent: () =>
          import('./features/facture/facture-list/facture-list.component').then(m => m.FactureListComponent),
        canActivate:   [permissionGuard],
        data:          { entity: 'Facture', method: 'GET' }
      },
    ]
  },

  // ─── 404
  {
    path:          '**',
    loadComponent: () =>
      import('./features/not-found/not-found.component')
        .then(m => m.NotFoundComponent)
  }

];
