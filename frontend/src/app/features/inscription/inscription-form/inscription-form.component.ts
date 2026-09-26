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
import { InscriptionService }               from '../inscription.service';
import { InscriptionCreateDTO,
         InscriptionUpdateDTO }             from '../models/inscription.model';
import { EtudiantService } from '../../etudiant/etudiant.service';
import { EtudiantResponseDTO } from '../../etudiant/models/etudiant.model';
import { SessionService } from '../../session/session.service';
import { SessionResponseDTO } from '../../session/models/session.model';
import { PaiementService } from '../../paiement/paiement.service';
import { PaiementResponseDTO } from '../../paiement/models/paiement.model';

@Component({
  selector:    'app-inscription-form',
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
  templateUrl: './inscription-form.component.html',
  styleUrl:    './inscription-form.component.css'
})
export class InscriptionFormComponent implements OnInit {

  form!:       FormGroup;
  isEditMode = false;
  isLoading  = false;
  isReadOnly = false;
  entityId?: number;
  isAutoFilled_etudiant = false;
  etudiantList: EtudiantResponseDTO[] = [];
  sessionList: SessionResponseDTO[] = [];
  paiementList: PaiementResponseDTO[] = [];

  constructor(
    private readonly fb:                FormBuilder,
    private readonly service:           InscriptionService,
    private readonly snackBar:          MatSnackBar,
    private readonly permissionService: PermissionService,
    private readonly etudiantService: EtudiantService,
    private readonly sessionService: SessionService,
    private readonly paiementService: PaiementService,
    private readonly dialogRef: MatDialogRef<InscriptionFormComponent>,
    @Inject(MAT_DIALOG_DATA) public dialogData: { id?: number; readOnly?: boolean }
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      statut: ['', [Validators.required]],
      dateInscription: ['', [Validators.required, Validators.minLength(2)]],
      etudiantId: [null, Validators.required],
      sessionId: [null, Validators.required],
      paiementId: [null, Validators.required],
    });

    const forcedReadOnly = this.dialogData?.readOnly ?? false;
    const id = this.dialogData?.id;
    if (id) {
      this.isEditMode = true;
      this.entityId   = +id;
      if (forcedReadOnly || (!this.permissionService.can('Inscription', 'PUT'))) {
        this.isReadOnly = true;
        this.form.disable();
      }
      this.loadEntity(this.entityId);
    } else {
      if (!this.permissionService.can('Inscription', 'POST')) {
        this.isReadOnly = true;
        this.form.disable();
      }
    }

    const connected_role_etudiant = localStorage.getItem('role') ?? '';
    const connected_id_etudiant   = localStorage.getItem('userId');
    if (connected_role_etudiant.toUpperCase() === 'ETUDIANT' && connected_id_etudiant) {
      this.form.patchValue({ etudiantId: +connected_id_etudiant });
      this.isAutoFilled_etudiant = true;
    } else if (this.permissionService.can('Etudiant', 'GET')) {
      this.etudiantService.findAll().subscribe({
        next:  data => this.etudiantList = data,
        error: ()   => this.etudiantList = []
      });
    }
    if (this.permissionService.can('Session', 'GET')) {
      this.sessionService.findAll().subscribe({
        next:  data => this.sessionList = data,
        error: err  => {
          if (err.status !== 403) {
            console.error('Erreur chargement Session:', err);
          }
          this.sessionList = [];
        }
      });
    }
    if (this.permissionService.can('Paiement', 'GET')) {
      this.paiementService.findAll().subscribe({
        next:  data => this.paiementList = data,
        error: err  => {
          if (err.status !== 403) {
            console.error('Erreur chargement Paiement:', err);
          }
          this.paiementList = [];
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
    if (raw.dateInscription instanceof Date) {
      value.dateInscription = raw.dateInscription.toISOString();
    } else if (raw.dateInscription) {
      value.dateInscription = new Date(raw.dateInscription).toISOString();
    }

    if (this.isEditMode && this.entityId) {
      this.service.update(this.entityId, value as InscriptionUpdateDTO).subscribe({
        next: () => {
          this.snackBar.open('Inscription modifié avec succès', 'Fermer',
            { duration: 3000, panelClass: ['snack-success'] });
          this.dialogRef.close(true);
        },
        error: (err: any) => this.handleSubmitError(err)
      });
    } else {
      this.service.create(value as InscriptionCreateDTO).subscribe({
        next: () => {
          this.snackBar.open('Inscription créé avec succès', 'Fermer',
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
