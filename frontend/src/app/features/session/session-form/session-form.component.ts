import { Component, OnInit, Inject } from '@angular/core';
import { CommonModule }                            from '@angular/common';
import { ReactiveFormsModule, FormBuilder,
         FormGroup, Validators }                   from '@angular/forms';
import { MatFormFieldModule }                      from '@angular/material/form-field';
import { MatInputModule }                          from '@angular/material/input';
import { MatSelectModule }                         from '@angular/material/select';
import { MatButtonModule }                         from '@angular/material/button';
import { MatIconModule }                           from '@angular/material/icon';
import { MatSnackBar, MatSnackBarModule }          from '@angular/material/snack-bar';
import { MatCheckboxModule }                       from '@angular/material/checkbox';
import { MatDatepickerModule }                     from '@angular/material/datepicker';
import { MatNativeDateModule }                     from '@angular/material/core';
import { PermissionService }                       from '../../../core/services/permission.service';
import { MatDialogRef, MAT_DIALOG_DATA,
         MatDialogModule }              from '@angular/material/dialog';
import { SessionService }               from '../session.service';
import { SessionCreateDTO,
         SessionUpdateDTO }             from '../models/session.model';
import { FormationService } from '../../formation/formation.service';
import { FormationResponseDTO } from '../../formation/models/formation.model';
import { FormateurService } from '../../formateur/formateur.service';
import { FormateurResponseDTO } from '../../formateur/models/formateur.model';
import { SalleService } from '../../salle/salle.service';
import { SalleResponseDTO } from '../../salle/models/salle.model';

@Component({
  selector:    'app-session-form',
  standalone:  true,
  imports:     [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    MatCheckboxModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatDialogModule,
  ],
  templateUrl: './session-form.component.html',
  styleUrl:    './session-form.component.css'
})
export class SessionFormComponent implements OnInit {

  form!:       FormGroup;
  isEditMode = false;
  isLoading  = false;
  isReadOnly = false;
  entityId?: number;
  formationList: FormationResponseDTO[] = [];
  isAutoFilled_formateur = false;
  formateurList: FormateurResponseDTO[] = [];
  salleList: SalleResponseDTO[] = [];

  constructor(
    private readonly fb:                FormBuilder,
    private readonly service:           SessionService,
    private readonly snackBar:          MatSnackBar,
    private readonly permissionService: PermissionService,
    private readonly formationService: FormationService,
    private readonly formateurService: FormateurService,
    private readonly salleService: SalleService,
    private readonly dialogRef: MatDialogRef<SessionFormComponent>,
    @Inject(MAT_DIALOG_DATA) public dialogData: { id?: number; readOnly?: boolean }
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      dateDebut: ['', [Validators.required, Validators.minLength(2)]],
      dateFin: ['', [Validators.required, Validators.minLength(2)]],
      formationId: [null, Validators.required],
      formateurId: [null, Validators.required],
      salleId: [null, Validators.required],
    });

    const forcedReadOnly = this.dialogData?.readOnly ?? false;
    const id = this.dialogData?.id;
    if (id) {
      this.isEditMode = true;
      this.entityId   = +id;
      if (forcedReadOnly || (!this.permissionService.can('Session', 'PUT'))) {
        this.isReadOnly = true;
        this.form.disable();
      }
      this.loadEntity(this.entityId);
    } else {
      if (!this.permissionService.can('Session', 'POST')) {
        this.isReadOnly = true;
        this.form.disable();
      }
    }

    if (this.permissionService.can('Formation', 'GET')) {
      this.formationService.findAll().subscribe({
        next:  data => this.formationList = data,
        error: err  => {
          if (err.status !== 403) {
            console.error('Erreur chargement Formation:', err);
          }
          this.formationList = [];
        }
      });
    }
    const connected_role_formateur = localStorage.getItem('role') ?? '';
    const connected_id_formateur   = localStorage.getItem('userId');
    if (connected_role_formateur.toUpperCase() === 'FORMATEUR' && connected_id_formateur) {
      this.form.patchValue({ formateurId: +connected_id_formateur });
      this.isAutoFilled_formateur = true;
    } else if (this.permissionService.can('Formateur', 'GET')) {
      this.formateurService.findAll().subscribe({
        next:  data => this.formateurList = data,
        error: ()   => this.formateurList = []
      });
    }
    if (this.permissionService.can('Salle', 'GET')) {
      this.salleService.findAll().subscribe({
        next:  data => this.salleList = data,
        error: err  => {
          if (err.status !== 403) {
            console.error('Erreur chargement Salle:', err);
          }
          this.salleList = [];
        }
      });
    }

    Object.keys(this.form.controls).forEach(key => {
      this.form.get(key)?.valueChanges.subscribe(() => {
        const ctrl = this.form.get(key);
        if (ctrl?.errors?.['serverError']) {
          const errors = { ...ctrl.errors };
          delete errors['serverError'];
          ctrl.setErrors(Object.keys(errors).length > 0 ? errors : null);
        }
      });
    });
  }

  loadEntity(id: number): void {
    this.isLoading = true;
    this.service.findById(id).subscribe({
      next:  data => {
        this.form.patchValue(data);
        this.isLoading = false;
        if (!this.isReadOnly && !this.permissionService.canEditRow(data)) {
          this.isReadOnly = true;
        }
        if (this.isReadOnly) this.form.disable();
      },
      error: () => {
        this.isLoading = false;
        this.snackBar.open('Erreur de chargement', 'Fermer',
          { duration: 3000, panelClass: ['snack-error'] });
      }
    });
  }

  onSubmit(): void {
    if (this.isReadOnly) return;
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }
    this.isLoading = true;
    const raw        = this.form.getRawValue();
    const value: any = { ...raw };
    if (raw.dateDebut instanceof Date) {
      value.dateDebut = raw.dateDebut.toISOString();
    } else if (raw.dateDebut) {
      value.dateDebut = new Date(raw.dateDebut).toISOString();
    }
    if (raw.dateFin instanceof Date) {
      value.dateFin = raw.dateFin.toISOString();
    } else if (raw.dateFin) {
      value.dateFin = new Date(raw.dateFin).toISOString();
    }

    if (this.isEditMode && this.entityId) {
      this.service.update(this.entityId, value as SessionUpdateDTO).subscribe({
        next: () => {
          this.snackBar.open('Session modifié avec succès', 'Fermer',
            { duration: 3000, panelClass: ['snack-success'] });
          this.dialogRef.close(true);
        },
        error: (err: any) => this.handleSubmitError(err)
      });
    } else {
      this.service.create(value as SessionCreateDTO).subscribe({
        next: () => {
          this.snackBar.open('Session créé avec succès', 'Fermer',
            { duration: 3000, panelClass: ['snack-success'] });
          this.dialogRef.close(true);
        },
        error: (err: any) => this.handleSubmitError(err)
      });
    }
  }

  private handleSubmitError(err: any): void {
    this.isLoading = false;
    const apiError = err?.error;

    if (apiError?.errors) {
      Object.keys(apiError.errors).forEach(fieldName => {
        const ctrl = this.form.get(fieldName);
        if (ctrl) {
          ctrl.setErrors({ serverError: apiError.errors[fieldName] });
          ctrl.markAsTouched();
        }
      });
      const messages = Object.values(apiError.errors).join(' — ');
      this.snackBar.open(messages, 'Fermer',
        { duration: 6000, panelClass: ['snack-error'] });
    } else {
      this.snackBar.open(
        apiError?.message ?? 'Erreur lors de l\'enregistrement',
        'Fermer',
        { duration: 4000, panelClass: ['snack-error'] }
      );
    }
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }

  isInvalid(field: string): boolean {
    const ctrl = this.form.get(field);
    return !!(ctrl && ctrl.invalid && ctrl.touched);
  }

  getErrorMessage(field: string): string {
    const ctrl = this.form.get(field);
    if (!ctrl?.errors) return '';
    if (ctrl.errors['serverError']) return ctrl.errors['serverError'];
    if (ctrl.errors['required'])     return 'Ce champ est requis';
    if (ctrl.errors['email'])        return 'Format email invalide';
    if (ctrl.errors['minlength'])
      return 'Trop court (min. ' + ctrl.errors['minlength'].requiredLength
             + ' caractères)';
    if (ctrl.errors['min'])          return 'Valeur doit être positive';
    return 'Champ invalide';
  }
}
