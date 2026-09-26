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
import { MatNativeDateModule }            from '@angular/material/core';
import { InscriptionFormComponent } from '../inscription-form/inscription-form.component';
import { InscriptionService }      from '../inscription.service';
import { InscriptionResponseDTO }  from '../models/inscription.model';

@Component({
  selector:    'app-inscription-list',
  standalone:  true,
  imports:     [
    CommonModule,
    MatTableModule, MatSortModule, MatPaginatorModule,
    MatButtonModule, MatIconModule,
    MatSnackBarModule, MatDialogModule, MatTooltipModule,
    MatNativeDateModule,
  ],
  templateUrl: './inscription-list.component.html',
  styleUrl:    './inscription-list.component.css'
})
export class InscriptionListComponent implements OnInit {

  displayedColumns: string[] = ['id', 'statut', 'dateInscription', 'actions'];
  dataSource = new MatTableDataSource<InscriptionResponseDTO>();
  isLoading  = false;
  totalCount = 0;

  enumStatsStatut: { label: string; count: number; color: string; bg: string; percent: number }[] = [];

  readonly entityName = 'Inscription';

  get canCreate(): boolean { return this.permissionService.can(this.entityName, 'POST'); }
  get canUpdate(): boolean { return this.permissionService.can(this.entityName, 'PUT'); }
  get canDelete(): boolean { return this.permissionService.can(this.entityName, 'DELETE'); }
  get canAssign(): boolean { return this.permissionService.canAssign(); }

  @ViewChild(MatSort)      sort!:      MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private readonly service:           InscriptionService,
    private readonly router:            Router,
    private readonly snackBar:          MatSnackBar,
    private readonly dialog:            MatDialog,
    readonly         permissionService: PermissionService
  ) {}

  ngOnInit(): void {
    this.loadData();
    this.loadStats();
  }

  loadData(): void {
    this.isLoading = true;
    this.service.findAll().subscribe({
      next: (data: InscriptionResponseDTO[]) => {
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
    this.service.statsByStatut().subscribe({
      next: data => this.buildEnumStatsStatut(data),
      error: () => {}
    });
  }

  onCreate(): void {
    const ref = this.dialog.open(InscriptionFormComponent, {
      width: '560px', maxWidth: '95vw', maxHeight: '90vh',
      panelClass: 'dialog-square', data: { readOnly: false }
    });
    ref.afterClosed().subscribe(result => {
      if (result) { this.loadData(); this.loadStats(); }
    });
  }

  onEdit(id: number): void {
    const ref = this.dialog.open(InscriptionFormComponent, {
      width: '560px', maxWidth: '95vw', maxHeight: '90vh',
      panelClass: 'dialog-square', data: { id, readOnly: false }
    });
    ref.afterClosed().subscribe(result => {
      if (result) { this.loadData(); this.loadStats(); }
    });
  }

  onView(id: number): void {
    const ref = this.dialog.open(InscriptionFormComponent, {
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

  buildEnumStatsStatut(data: {[key: string]: number}): void {
    const palette = [
      { color: '#3B82F6', bg: '#EFF6FF' },
      { color: '#10B981', bg: '#ECFDF5' },
      { color: '#F59E0B', bg: '#FFFBEB' },
      { color: '#EF4444', bg: '#FEF2F2' },
      { color: '#8B5CF6', bg: '#F5F3FF' },
      { color: '#EC4899', bg: '#FDF2F8' },
      { color: '#14B8A6', bg: '#F0FDFA' },
      { color: '#F97316', bg: '#FFF7ED' },
      { color: '#6366F1', bg: '#EEF2FF' },
      { color: '#84CC16', bg: '#F7FEE7' },
    ];
    const total = Object.values(data).reduce((a, b) => a + b, 0);
    this.enumStatsStatut = Object.entries(data).map(([key, val], i) => ({
      label:   key,
      count:   val as number,
      color:   palette[i % palette.length].color,
      bg:      palette[i % palette.length].bg,
      percent: total > 0 ? Math.round(((val as number) / total) * 100) : 0,
    }));
  }

}
