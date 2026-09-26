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
import { FormateurExterneService }      from '../formateurexterne.service';
import { FormateurExterneResponseDTO }  from '../models/formateurexterne.model';

@Component({
  selector:    'app-formateurexterne-list',
  standalone:  true,
  imports:     [
    CommonModule,
    MatTableModule, MatSortModule, MatPaginatorModule,
    MatButtonModule, MatIconModule,
    MatSnackBarModule, MatDialogModule, MatTooltipModule,
  ],
  templateUrl: './formateurexterne-list.component.html',
  styleUrl:    './formateurexterne-list.component.css'
})
export class FormateurExterneListComponent implements OnInit {

  displayedColumns: string[] = ['id', 'tarifJournalier', 'actions'];
  dataSource = new MatTableDataSource<FormateurExterneResponseDTO>();
  isLoading  = false;
  totalCount = 0;


  readonly entityName = 'FormateurExterne';

  get canCreate(): boolean { return this.permissionService.can(this.entityName, 'POST'); }
  get canUpdate(): boolean { return this.permissionService.can(this.entityName, 'PUT'); }
  get canDelete(): boolean { return this.permissionService.can(this.entityName, 'DELETE'); }
  get canAssign(): boolean { return this.permissionService.canAssign(); }

  @ViewChild(MatSort)      sort!:      MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private readonly service:           FormateurExterneService,
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
      next: (data: FormateurExterneResponseDTO[]) => {
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
    this.router.navigate(['/formateurexterne/new']);
  }

  onEdit(id: number): void {
    this.router.navigate(['/formateurexterne/edit', id]);
  }

  onView(id: number): void {
    this.router.navigate(['/formateurexterne/edit', id]);
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
