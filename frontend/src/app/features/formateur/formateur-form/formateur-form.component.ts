import { Component, OnInit } from '@angular/core';
import { CommonModule }                            from '@angular/common';
import { ReactiveFormsModule, FormBuilder,
         FormGroup, Validators }                   from '@angular/forms';
import { Router, ActivatedRoute }                  from '@angular/router';
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
import { FormateurService }               from '../formateur.service';
import { FormateurCreateDTO,
         FormateurUpdateDTO }             from '../models/formateur.model';
import { DepartementService } from '../../departement/departement.service';
import { DepartementResponseDTO } from '../../departement/models/departement.model';

@Component({
  selector:    'app-formateur-form',
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
  ],
  templateUrl: './formateur-form.component.html',
  styleUrl:    './formateur-form.component.css'
})
export class FormateurFormComponent implements OnInit {

  form!:       FormGroup;
  isEditMode = false;
  isLoading  = false;
  isReadOnly = false;
  entityId?: number;
  departementList: DepartementResponseDTO[] = [];

  constructor(
    private readonly fb:                FormBuilder,
    private readonly service:           FormateurService,
    private readonly router:            Router,
    private readonly route:             ActivatedRoute,
    private readonly snackBar:          MatSnackBar,
    private readonly permissionService: PermissionService,
    private readonly departementService: DepartementService
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      username: ['', [Validators.required]],
      email:    ['', [Validators.required, Validators.email]],
      nom: ['', [Validators.required, Validators.minLength(2)]],
      prenom: ['', [Validators.required, Validators.minLength(2)]],
      anneesExperience: [null, [Validators.required, Validators.min(0)]],
      departementId: [null, Validators.required],
    });

    const forcedReadOnly = false;
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.entityId   = +id;
      if (forcedReadOnly || (!this.permissionService.can('Formateur', 'PUT') && !this.isOwnRecord())) {
        this.isReadOnly = true;
        this.form.disable();
      }
      if (this.isOwnRecord()) {
        this.form.get('username')?.disable();
      }
      this.loadEntity(this.entityId);
    } else {
      if (!this.permissionService.can('Formateur', 'POST')) {
        this.isReadOnly = true;
        this.form.disable();
      }
    }

    if (this.permissionService.can('Departement', 'GET')) {
      this.departementService.findAll().subscribe({
        next:  data => this.departementList = data,
        error: err  => {
          if (err.status !== 403) {
            console.error('Erreur chargement Departement:', err);
          }
          this.departementList = [];
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

  private isOwnRecord(): boolean {
    const userId = localStorage.getItem('userId');
    const role   = localStorage.getItem('role') ?? '';
    return role !== 'ADMIN' && !!this.entityId && !!userId
        && String(this.entityId) === userId;
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

    if (this.isEditMode && this.entityId) {
      this.service.update(this.entityId, value as FormateurUpdateDTO).subscribe({
        next: () => {
          this.snackBar.open('Formateur modifié avec succès', 'Fermer',
            { duration: 3000, panelClass: ['snack-success'] });
          this.router.navigate(['/formateur']);
        },
        error: (err: any) => this.handleSubmitError(err)
      });
    } else {
      this.service.create(value as FormateurCreateDTO).subscribe({
        next: () => {
          this.snackBar.open('Formateur créé avec succès', 'Fermer',
            { duration: 3000, panelClass: ['snack-success'] });
          this.router.navigate(['/formateur']);
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
    this.router.navigate(['/formateur']);
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
