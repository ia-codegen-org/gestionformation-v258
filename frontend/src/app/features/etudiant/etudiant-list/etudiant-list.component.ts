import { Component, OnInit, ViewChild } from '@angular/core';
import { CommonModule }                   from '@angular/common';
import { Router } from '@angular/router';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatSortModule, MatSort }         from '@angular/material/sort';
import { MatPaginatorModule, MatPaginator } from '@angular/material/paginator';
import { MatButtonModule }                from '@angular/material/button';
import { MatIconModule }                  from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule }     from '@angular/material/dialog';
import { MatTooltipModule }               from '@angular/material/tooltip';
import { ConfirmDialogComponent }         from '../../../shared/confirm-dialog/confirm-dialog.component';
import { AssignDialogComponent }          from '../../../shared/assign-dialog/assign-dialog.component';
import { PermissionService }              from '../../../core/services/permission.service';
import { SessionService } from '../../session/session.service';
import { SessionResponseDTO } from '../../session/models/session.model';
import { EtudiantService }      from '../etudiant.service';
import { EtudiantResponseDTO }  from '../models/etudiant.model';

@Component({
  selector:    'app-etudiant-list',
  standalone:  true,
  imports:     [
    CommonModule,
    MatTableModule, MatSortModule, MatPaginatorModule,
    MatButtonModule, MatIconModule,
    MatSnackBarModule, MatDialogModule, MatTooltipModule,
  ],
  templateUrl: './etudiant-list.component.html',
  styleUrl:    './etudiant-list.component.css'
})
export class EtudiantListComponent implements OnInit {

  displayedColumns: string[] = ['id', 'nom', 'prenom', 'dateNaissance', 'noteMoyenne', 'dureeTotaleFormation', 'nbCertificats', 'actions'];
  dataSource = new MatTableDataSource<EtudiantResponseDTO>();
  isLoading  = false;
  totalCount = 0;


  readonly entityName = 'Etudiant';

  get canCreate(): boolean { return this.permissionService.can(this.entityName, 'POST'); }
  get canUpdate(): boolean { return this.permissionService.can(this.entityName, 'PUT'); }
  get canDelete(): boolean { return this.permissionService.can(this.entityName, 'DELETE'); }
  get canAssign(): boolean { return this.permissionService.canAssign(); }

  @ViewChild(MatSort)      sort!:      MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private readonly service:           EtudiantService,
    private readonly router:            Router,
    private readonly snackBar:          MatSnackBar,
    private readonly dialog:            MatDialog,
    readonly         permissionService: PermissionService,
    private readonly sessionService: SessionService
  ) {}

  ngOnInit(): void {
    this.loadData();
    this.loadStats();
  }

  loadData(): void {
    this.isLoading = true;
    this.service.findAll().subscribe({
      next: (data: EtudiantResponseDTO[]) => {
        this.dataSource.data      = data;
        this.dataSource.sort      = this.sort;
        this.dataSource.paginator = this.paginator;
        this.isLoading            = false;
      },
      error: () => {
        this.isLoading = false;
        this.snackBar.open('Erreur lors du chargement', 'Fermer',
          { duration: 3000, panelClass: ['snack-error'] });
      }
    });
  }

  loadStats(): void {
    this.service.count().subscribe({
      next: count => this.totalCount = count,
      error: ()   => this.totalCount = 0
    });
  }

  onCreate(): void {
    this.router.navigate(['/etudiant/new']);
  }

  onEdit(id: number): void {
    this.router.navigate(['/etudiant/edit', id]);
  }

  onView(id: number): void {
    this.router.navigate(['/etudiant/edit', id]);
  }

  onDelete(id: number): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '420px',
      data: { title: 'Confirmer la suppression',
              message: 'Êtes-vous sûr de vouloir supprimer cet enregistrement ? Cette action est irréversible.' }
    });
    dialogRef.afterClosed().subscribe((confirmed: boolean) => {
      if (!confirmed) return;
      this.service.delete(id).subscribe({
        next: () => {
          this.snackBar.open('Supprimé avec succès', 'Fermer',
            { duration: 3000, panelClass: ['snack-success'] });
          this.loadData();
          this.loadStats();
        },
        error: (err: any) => {
          const status  = err?.status;
          const message = err?.error?.message;
          if (status === 409) {
            this.snackBar.open(
              message ?? 'Impossible de supprimer : des enregistrements liés existent.',
              'Fermer', { duration: 6000, panelClass: ['snack-error'] });
          } else {
            this.snackBar.open('Erreur lors de la suppression', 'Fermer',
              { duration: 3000, panelClass: ['snack-error'] });
          }
        }
      });
    });
  }

  canEdit(row: any): boolean {
    return this.canUpdate && this.permissionService.canEditRow(row);
  }

  getEnumClass(value: string): string {
    if (!value) return 'tag-default';
    const colors = ['tag-blue','tag-green','tag-orange',
                     'tag-purple','tag-red','tag-teal','tag-pink','tag-indigo'];
    let hash = 0;
    for (let i = 0; i < value.length; i++) {
      hash = (value.codePointAt(i) ?? 0) + ((hash << 5) - hash);
    }
    return colors[Math.abs(hash) % colors.length];
  }

  onAssignSession(entityId: number): void {
    this.sessionService.findAll().subscribe({
      next: (list: SessionResponseDTO[]) => {
        const dialogRef = this.dialog.open(AssignDialogComponent, {
          width: '480px',
          data: {
            title:        'Assigner Session',
            items:        list,
            displayField: 'titre'
          }
        });
        dialogRef.afterClosed()
          .subscribe((selectedId: number | null) => {
            if (!selectedId) return;
            this.service.addSessionToEtudiant(entityId, selectedId).subscribe({
              next: () => {
                this.snackBar.open('Session assigné avec succès', 'Fermer',
                  { duration: 3000, panelClass: ['snack-success'] });
                this.loadData();
                this.loadStats();
              },
              error: () => this.snackBar.open(
                'Erreur lors de l\'assignation', 'Fermer',
                { duration: 3000, panelClass: ['snack-error'] })
            });
          });
      },
      error: () => this.snackBar.open(
        'Impossible de charger les Sessions', 'Fermer',
        { duration: 3000, panelClass: ['snack-error'] })
    });
  }

  onUnassignSession(entityId: number): void {
    this.service.getSessionsOfEtudiant(entityId).subscribe({
      next: (list: SessionResponseDTO[]) => {
        if (!list.length) {
          this.snackBar.open('Aucun Session assigné à retirer', 'Fermer',
            { duration: 3000 });
          return;
        }
        const dialogRef = this.dialog.open(AssignDialogComponent, {
          width: '480px',
          data: {
            title:        'Retirer Session',
            items:        list,
            displayField: 'titre'
          }
        });
        dialogRef.afterClosed()
          .subscribe((selectedId: number | null) => {
            if (!selectedId) return;
            this.service.removeSessionFromEtudiant(entityId, selectedId).subscribe({
              next: () => {
                this.snackBar.open('Session retiré avec succès', 'Fermer',
                  { duration: 3000, panelClass: ['snack-success'] });
                this.loadData();
                this.loadStats();
              },
              error: () => this.snackBar.open(
                'Erreur lors du retrait', 'Fermer',
                { duration: 3000, panelClass: ['snack-error'] })
            });
          });
      },
      error: () => this.snackBar.open(
        'Impossible de charger les Sessions assignés', 'Fermer',
        { duration: 3000, panelClass: ['snack-error'] })
    });
  }

  onBlock(id: number): void {
    this.service.blockUser(id).subscribe({
      next: () => {
        this.snackBar.open('Compte bloqué avec succès', 'Fermer',
          { duration: 3000, panelClass: ['snack-success'] });
        this.loadData();
      },
      error: () => this.snackBar.open('Erreur lors du blocage', 'Fermer',
        { duration: 3000, panelClass: ['snack-error'] })
    });
  }

  onUnblock(id: number): void {
    this.service.unblockUser(id).subscribe({
      next: () => {
        this.snackBar.open('Compte débloqué avec succès', 'Fermer',
          { duration: 3000, panelClass: ['snack-success'] });
        this.loadData();
      },
      error: () => this.snackBar.open('Erreur lors du déblocage', 'Fermer',
        { duration: 3000, panelClass: ['snack-error'] })
    });
  }

}
