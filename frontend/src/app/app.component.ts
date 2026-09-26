import { Component, OnInit, signal } from '@angular/core';
import { RouterOutlet,
         RouterLink,
         RouterLinkActive,
         Router,
         NavigationEnd }       from '@angular/router';
import { CommonModule }        from '@angular/common';
import { MatToolbarModule }    from '@angular/material/toolbar';
import { MatSidenavModule }    from '@angular/material/sidenav';
import { MatListModule }       from '@angular/material/list';
import { MatTooltipModule }    from '@angular/material/tooltip';
import { MatIconModule }       from '@angular/material/icon';
import { MatButtonModule }     from '@angular/material/button';
import { filter }             from 'rxjs/operators';
import { PermissionService }  from './core/services/permission.service';
import { AuthService } from './core/services/auth.service';

@Component({
  selector:    'app-root',
  standalone:  true,
  imports:     [
    RouterOutlet,
    RouterLink,
    RouterLinkActive,
    CommonModule,
    MatToolbarModule,
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule
  ],
  templateUrl: './app.component.html',
  styleUrl:    './app.component.css'
})
export class AppComponent implements OnInit {

  title              = 'gestionformation';
  projectDisplayName = 'GestionFormation';
  domainIcon         = 'school';
  domainColor        = '#7C3AED';
  sidenavOpen        = signal(true);
  isAuthPage         = false;
  usersMenuOpen      = signal(false);
  isAdmin            = false;

  navLinks = [
    { path: '/formation', label: 'Formations', icon: 'book', entity: 'Formation' },
    { path: '/session', label: 'Sessions', icon: 'event', entity: 'Session' },
    { path: '/salle', label: 'Salles', icon: 'meeting_room', entity: 'Salle' },
    { path: '/batiment', label: 'Batiments', icon: 'apartment', entity: 'Batiment' },
    { path: '/departement', label: 'Departements', icon: 'domain', entity: 'Departement' },
    { path: '/categorie', label: 'Categories', icon: 'category', entity: 'Categorie' },
    { path: '/inscription', label: 'Inscriptions', icon: 'assignment', entity: 'Inscription' },
    { path: '/evaluation', label: 'Evaluations', icon: 'assessment', entity: 'Evaluation' },
    { path: '/certificat', label: 'Certificats', icon: 'verified', entity: 'Certificat' },
    { path: '/paiement', label: 'Paiements', icon: 'payments', entity: 'Paiement' },
    { path: '/facture', label: 'Factures', icon: 'receipt', entity: 'Facture' },
  ];

  actorLinks = [
    { path: '/formateur', label: 'Formateurs', icon: 'person', entity: 'Formateur' },
    { path: '/formateurinterne', label: 'FormateurInternes', icon: 'person', entity: 'FormateurInterne' },
    { path: '/formateurexterne', label: 'FormateurExternes', icon: 'person', entity: 'FormateurExterne' },
    { path: '/etudiant', label: 'Etudiants', icon: 'school', entity: 'Etudiant' },
  ];

  visibleNavLinks:   any[] = [];
  visibleActorLinks: any[] = [];
  hasVisibleActors       = false;
  private lastLoadedRole = '';

  constructor(
    private readonly router:            Router,
    private readonly permissionService: PermissionService,
    private readonly authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.router.events.pipe(
      filter(e => e instanceof NavigationEnd)
    ).subscribe((e: any) => {
      this.isAuthPage = e.url.includes('/login')
                     || e.url.includes('/change-password')
                     || e.url.includes('/forgot-password')
                     || e.url.includes('/reset-password')
                     || e.url.includes('/account-blocked');
      this.refreshPermissionsIfNeeded();
    });

    this.refreshPermissionsIfNeeded();
  }

  private refreshPermissionsIfNeeded(): void {
    const role = localStorage.getItem('role') ?? '';
    this.isAdmin = role === 'ADMIN';

    if (!role) {
      this.lastLoadedRole    = '';
      this.visibleNavLinks   = [];
      this.visibleActorLinks = [];
      this.hasVisibleActors  = false;
      return;
    }

    if (role === this.lastLoadedRole) {
      this.computeVisibleLinks();
      return;
    }

    this.lastLoadedRole = role;
    this.permissionService.loadMyPermissions().subscribe({
      next:  () => this.computeVisibleLinks(),
      error: () => this.computeVisibleLinks()
    });
  }

  private computeVisibleLinks(): void {
    const isAdmin = this.isAdmin;

    this.visibleNavLinks = isAdmin
      ? [...this.navLinks]
      : this.navLinks.filter((link: any) =>
          this.permissionService.can(link.entity, 'GET')
        );

    this.visibleActorLinks = isAdmin
      ? [...this.actorLinks]
      : this.actorLinks.filter((link: any) =>
          this.permissionService.can(link.entity, 'GET')
        );

    this.hasVisibleActors = this.visibleActorLinks.length > 0;
  }

  toggleSidenav(): void {
    this.sidenavOpen.update(v => !v);
  }

  toggleUsersMenu(): void {
    this.usersMenuOpen.update(v => !v);
  }

  logout(): void {
    this.authService.logout();
  }

}
