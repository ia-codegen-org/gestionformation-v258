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
import { EtudiantService } from '../../etudiant/etudiant.service';
import { EtudiantResponseDTO } from '../../etudiant/models/etudiant.model';
import { MatNativeDateModule }            from '@angular/material/core';
import { SessionFormComponent } from '../session-form/session-form.component';
import { SessionService }      from '../session.service';
import { SessionResponseDTO }  from '../models/session.model';

@Component({
  selector:    'app-session-list',
  standalone:  true,
  imports:     [
    CommonModule,
    MatTableModule, MatSortModule, MatPaginatorModule,
    MatButtonModule, MatIconModule,
    MatSnackBarModule, MatDialogModule, MatTooltipModule,
    MatNativeDateModule,
  ],
  templateUrl: './session-list.component.html',
  styleUrl:    './session-list.component.css'
})
export class SessionListComponent implements OnInit {

  displayedColumns: string[] = ['id', 'dateDebut', 'dateFin', 'nbEtudiants', 'coutTotal', 'actions'];
  dataSource = new MatTableDataSource<SessionResponseDTO>();
  isLoading  = false;
  totalCount = 0;


  readonly entityName = 'Session';

  get canCreate(): boolean { return this.permissionService.can(this.entityName, 'POST'); }
  get canUpdate(): boolean { return this.permissionService.can(this.entityName, 'PUT'); }
  get canDelete(): boolean { return this.permissionService.can(this.entityName, 'DELETE'); }
  get canAssign(): boolean { return this.permissionService.canAssign(); }

  @ViewChild(MatSort)      sort!:      MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private readonly service:           SessionService,
    private readonly router:            Router,
    private readonly snackBar:          MatSnackBar,
    private readonly dialog:            MatDialog,
    readonly         permissionService: PermissionService,
    private readonly etudiantService: EtudiantService
  ) {}

  ngOnInit(): void {
    this.loadData();
    this.loadStats();
  }

  loadData(): void {
    this.isLoading = true;
    this.service.findAll().subscribe({
      next: (data: SessionResponseDTO[]) => {
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
    const ref = this.dialog.open(SessionFormComponent, {
      width: '560px', maxWidth: '95vw', maxHeight: '90vh',
      panelClass: 'dialog-square', data: { readOnly: false }
    });
    ref.afterClosed().subscribe(result => {
      if (result) { this.loadData(); this.loadStats(); }
    });
  }

  onEdit(id: number): void {
    const ref = this.dialog.open(SessionFormComponent, {
      width: '560px', maxWidth: '95vw', maxHeight: '90vh',
      panelClass: 'dialog-square', data: { id, readOnly: false }
    });
    ref.afterClosed().subscribe(result => {
      if (result) { this.loadData(); this.loadStats(); }
    });
  }

  onView(id: number): void {
    const ref = this.dialog.open(SessionFormComponent, {
      width: '560px', maxWidth: '95vw', maxHeight: '90vh',
      panelClass: 'dialog-square', data: { id, readOnly: true }
    });
    ref.afterClosed().subscribe(() => this.loadData());
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

  onAssignEtudiant(entityId: number): void {
    this.etudiantService.findAll().subscribe({
      next: (list: EtudiantResponseDTO[]) => {
        const dialogRef = this.dialog.open(AssignDialogComponent, {
          width: '480px',
          data: {
            title:        'Assigner Etudiant',
            items:        list,
            displayField: 'nom'
          }
        });
        dialogRef.afterClosed()
          .subscribe((selectedId: number | null) => {
            if (!selectedId) return;
            this.service.addEtudiantToSession(entityId, selectedId).subscribe({
              next: () => {
                this.snackBar.open('Etudiant assigné avec succès', 'Fermer',
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
        'Impossible de charger les Etudiants', 'Fermer',
        { duration: 3000, panelClass: ['snack-error'] })
    });
  }

  onUnassignEtudiant(entityId: number): void {
    this.service.getEtudiantsOfSession(entityId).subscribe({
      next: (list: EtudiantResponseDTO[]) => {
        if (!list.length) {
          this.snackBar.open('Aucun Etudiant assigné à retirer', 'Fermer',
            { duration: 3000 });
          return;
        }
        const dialogRef = this.dialog.open(AssignDialogComponent, {
          width: '480px',
          data: {
            title:        'Retirer Etudiant',
            items:        list,
            displayField: 'nom'
          }
        });
        dialogRef.afterClosed()
          .subscribe((selectedId: number | null) => {
            if (!selectedId) return;
            this.service.removeEtudiantFromSession(entityId, selectedId).subscribe({
              next: () => {
                this.snackBar.open('Etudiant retiré avec succès', 'Fermer',
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
        'Impossible de charger les Etudiants assignés', 'Fermer',
        { duration: 3000, panelClass: ['snack-error'] })
    });
  }

}
