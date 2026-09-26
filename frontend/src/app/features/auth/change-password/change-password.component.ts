import { Component, OnInit }       from '@angular/core';
import { CommonModule }                from '@angular/common';
import { ReactiveFormsModule,
         FormBuilder, FormGroup,
         Validators,
         AbstractControl }             from '@angular/forms';
import { Router }                      from '@angular/router';
import { MatFormFieldModule }          from '@angular/material/form-field';
import { MatInputModule }              from '@angular/material/input';
import { MatButtonModule }             from '@angular/material/button';
import { MatIconModule }               from '@angular/material/icon';
import { MatSnackBar,
         MatSnackBarModule }           from '@angular/material/snack-bar';
import { MatProgressSpinnerModule }    from '@angular/material/progress-spinner';
import { AuthService }                 from '../../../core/services/auth.service';

@Component({
  selector:    'app-change-password',
  standalone:  true,
  imports:     [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    MatProgressSpinnerModule
  ],
  templateUrl: './change-password.component.html',
  styleUrl:    './change-password.component.css'
})
export class ChangePasswordComponent implements OnInit {

  form!:                FormGroup;
  isLoading           = false;
  hideOldPassword     = true;
  hideNewPassword     = true;
  hideConfirmPassword = true;
  showPasswordErrors  = false;

  // ─── Règles de validation
  passwordRules = [
    { label: 'Au moins 8 caractères',         check: (v: string) => v.length >= 8 },
    { label: 'Au moins une majuscule (A-Z)',   check: (v: string) => /[A-Z]/.test(v) },
    { label: 'Au moins une minuscule (a-z)',   check: (v: string) => /[a-z]/.test(v) },
    { label: 'Au moins un chiffre (0-9)',      check: (v: string) => /[0-9]/.test(v) },
    { label: 'Au moins un symbole (!@#$%...)', check: (v: string) => /[!@#$%^&*()_+\-=\[\]{};:\'"|,.<>\/?]/.test(v) }
  ];

  constructor(
    private readonly fb:          FormBuilder,
    private readonly authService: AuthService,
    private readonly router:      Router,
    private readonly snackBar:    MatSnackBar
  ) {}

  ngOnInit(): void {
    this.form = this.fb.group({
      oldPassword:     ['', [Validators.required]],
      newPassword:     ['', [Validators.required]],
      confirmPassword: ['', Validators.required]
    }, { validators: this.passwordMatchValidator });
  }

  // ─── Validator confirm = new
  passwordMatchValidator(group: AbstractControl) {
    const newPw     = group.get('newPassword')?.value;
    const confirmPw = group.get('confirmPassword')?.value;
    return newPw === confirmPw ? null : { passwordMismatch: true };
  }

  // ─── Valeur newPassword en temps réel
  get newPasswordValue(): string {
    return this.form?.get('newPassword')?.value ?? '';
  }

  // ─── Force du mot de passe
  get passwordStrength(): number {
    return this.passwordRules.filter(r => r.check(this.newPasswordValue)).length;
  }

  get strengthLabel(): string {
    const s = this.passwordStrength;
    if (s <= 1) return 'Très faible';
    if (s === 2) return 'Faible';
    if (s === 3) return 'Moyen';
    if (s === 4) return 'Fort';
    return 'Très fort';
  }

  get strengthColor(): string {
    const s = this.passwordStrength;
    if (s <= 1) return '#dc3545';
    if (s === 2) return '#fd7e14';
    if (s === 3) return '#ffc107';
    if (s === 4) return '#20c997';
    return '#198754';
  }

  get strengthPercent(): number {
    return (this.passwordStrength / this.passwordRules.length) * 100;
  }

  // ✅ Bouton toujours actif
  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }

    // ✅ Vérifier règles mot de passe
    if (this.passwordStrength < this.passwordRules.length) {
      this.showPasswordErrors = true;
      this.snackBar.open(
        'Le mot de passe ne respecte pas toutes les règles de sécurité.',
        'Fermer',
        { duration: 4000, panelClass: ['snack-error'] }
      );
      return;
    }

    this.showPasswordErrors = false;
    this.isLoading = true;
    const { oldPassword, newPassword } = this.form.value;

    this.authService.changePassword({ oldPassword, newPassword }).subscribe({
      next: () => {
        this.isLoading = false;
        this.snackBar.open(
          'Mot de passe modifié avec succès',
          'Fermer',
          { duration: 3000, panelClass: ['snack-success'] }
        );
        this.router.navigate(['/']);
      },
      error: () => {
        this.isLoading = false;
        this.snackBar.open(
          'Erreur lors du changement de mot de passe',
          'Fermer',
          { duration: 3000, panelClass: ['snack-error'] }
        );
      }
    });
  }

  isInvalid(field: string): boolean {
    const ctrl = this.form.get(field);
    return !!(ctrl && ctrl.invalid && ctrl.touched);
  }

  get passwordMismatch(): boolean {
    return this.form.hasError('passwordMismatch')
        && !!this.form.get('confirmPassword')?.touched;
  }
}
