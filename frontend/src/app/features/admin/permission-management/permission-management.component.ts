import { Component, OnInit } from '@angular/core';
import { CommonModule }             from '@angular/common';
import { MatIconModule }            from '@angular/material/icon';
import { PermissionMatrixService,
         PermissionEntry }           from '../../../core/services/permission-matrix.service';

// ✅ Une ligne par entité pour le rôle actif — 4 actions
// (Voir/Créer/Modifier/Supprimer) au lieu d'une ligne par permission.
interface EntityPermissionRow {
  entityName: string;
  icon:       string;
  readScope?: string;
  get:        PermissionEntry | undefined;
  post:       PermissionEntry | undefined;
  put:        PermissionEntry | undefined;
  del:        PermissionEntry | undefined;
}

@Component({
  selector:    'app-permission-management',
  standalone:  true,
  imports:     [CommonModule, MatIconModule],
  templateUrl: './permission-management.component.html',
  styleUrl:    './permission-management.component.css'
})
export class PermissionManagementComponent implements OnInit {

  permissions: PermissionEntry[] = [];
  loading  = false;
  error    = '';

  // ✅ Même mécanisme de branding que AppComponent/HomeComponent —
  // couleur du domaine liée en template via [style.background]/[style.color].
  domainColor = '#7C3AED';

  // ✅ ADMIN exclu de la liste des rôles (voir recomputeRoles ci-dessous) —
  // accès toujours complet et non éditable, garde-fou dupliqué côté
  // serveur dans PermissionService.updateAllowed.
  private _activeRole = '';

  // ✅ Propriétés calculées une seule fois par chargement/changement de
  // rôle — PAS des getters. Un getter appelé depuis *ngFor recrée un
  // nouveau tableau d'objets à CHAQUE cycle de détection de changement
  // (le moindre clic en déclenche un) ; sans identité stable, Angular
  // détruit et recrée tous les mat-slide-toggle à chaque cycle au lieu
  // de les réutiliser, jusqu'à geler le thread principal.
  roles: string[] = [];
  entitiesForActiveRole: EntityPermissionRow[] = [];
  hasReadScopeColumn = false;

  private readonly entityIcons: Record<string, string> = {
    'Formateur': 'person',
    'FormateurInterne': 'person',
    'FormateurExterne': 'person',
    'Etudiant': 'school',
    'Formation': 'book',
    'Session': 'event',
    'Salle': 'meeting_room',
    'Batiment': 'apartment',
    'Departement': 'domain',
    'Categorie': 'category',
    'Inscription': 'assignment',
    'Evaluation': 'assessment',
    'Certificat': 'verified',
    'Paiement': 'payments',
    'Facture': 'receipt',
  };

  constructor(
    private readonly permissionMatrixService: PermissionMatrixService
  ) {}

  ngOnInit(): void {
    this.loadPermissions();
  }

  loadPermissions(): void {
    this.loading = true;
    this.error   = '';
    this.permissionMatrixService.findAll().subscribe({
      next: (data) => {
        this.permissions = data;
        this.loading     = false;
        this.recomputeRoles();
      },
      error: () => {
        this.error   = 'Impossible de charger les permissions.';
        this.loading = false;
      }
    });
  }

  // ✅ Mutation directe de l'objet PermissionEntry référencé à la fois par
  // this.permissions ET par entitiesForActiveRole (même instance) — pas
  // besoin de recalculer les lignes juste pour refléter un toggle.
  toggle(permission: PermissionEntry, event: Event): void {
    const checked  = (event.target as HTMLInputElement).checked;
    const previous = permission.allowed;
    permission.allowed = checked;

    this.permissionMatrixService.updateAllowed(permission.id, checked)
      .subscribe({
        next: (updated) => { permission.allowed = updated.allowed; },
        error: () => {
          // ✅ Revert — échec serveur, on ne garde pas le changement local
          permission.allowed = previous;
          this.error = 'Impossible de mettre à jour cette permission.';
        }
      });
  }

  get activeRole(): string {
    return this._activeRole;
  }

  selectRole(role: string): void {
    this._activeRole = role;
    this.recomputeRows();
  }

  // ✅ Rôles distincts présents dans la matrice, ADMIN exclu — un onglet
  // par rôle réellement présent dans ce projet, pas une liste codée en dur.
  private recomputeRoles(): void {
    const roles = new Set<string>();
    this.permissions.forEach(p => { if (p.role !== 'ADMIN') roles.add(p.role); });
    this.roles = Array.from(roles).sort((a, b) => a.localeCompare(b));
    if (!this._activeRole || !this.roles.includes(this._activeRole)) {
      this._activeRole = this.roles[0] ?? '';
    }
    this.recomputeRows();
  }

  // ✅ Lignes groupées par entité pour le rôle actif.
  private recomputeRows(): void {
    const rolePerms   = this.permissions.filter(p => p.role === this._activeRole);
    const entityNames = Array.from(new Set(rolePerms.map(p => p.entityName)));
    this.entitiesForActiveRole = entityNames.map(entityName => {
      const rows = rolePerms.filter(p => p.entityName === entityName);
      return {
        entityName,
        icon:       this.entityIcons[entityName] ?? 'table_rows',
        readScope:  rows[0]?.readScope,
        get:        rows.find(p => p.action === 'GET'),
        post:       rows.find(p => p.action === 'POST'),
        put:        rows.find(p => p.action === 'PUT'),
        del:        rows.find(p => p.action === 'DELETE'),
      };
    });
    // ✅ Colonne "Portée" masquée tant qu'aucune entité n'a de readScope —
    // non fourni par le backend actuel (voir PermissionEntry.readScope).
    this.hasReadScopeColumn = this.entitiesForActiveRole.some(row => !!row.readScope);
  }

  trackByEntity(_index: number, row: EntityPermissionRow): string {
    return row.entityName;
  }

  scopeLabel(scope: string | undefined): string {
    return scope === 'OWN' ? 'Personnel' : 'Global';
  }
}
