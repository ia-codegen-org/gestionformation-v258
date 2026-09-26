import { Component, OnInit }       from '@angular/core';
import { CommonModule }                from '@angular/common';
import { ReactiveFormsModule,
         FormBuilder, FormGroup,
         Validators }                  from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { MatFormFieldModule }          from '@angular/material/form-field';
import { MatInputModule }              from '@angular/material/input';
import { MatButtonModule }             from '@angular/material/button';
import { MatIconModule }               from '@angular/material/icon';
import { MatSnackBar,
         MatSnackBarModule }           from '@angular/material/snack-bar';
import { MatProgressSpinnerModule }    from '@angular/material/progress-spinner';
import { AuthService }                 from '../../../core/services/auth.service';

@Component({
  selector:    'app-login',
  standalone:  true,
  imports:     [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule,
    MatProgressSpinnerModule,
  RouterLink
  ],
  templateUrl: './login.component.html',
  styleUrl:    './login.component.css'
})
export class LoginComponent implements OnInit {

  form!:         FormGroup;
  isLoading    = false;
  hidePassword = true;

  constructor(
    private readonly fb:          FormBuilder,
    private readonly authService: AuthService,
    private readonly router:      Router,
    private readonly snackBar:    MatSnackBar
  ) {}

  ngOnInit(): void {
    if (this.authService.isLoggedIn()) {
      this.router.navigate(['/']);
      return;
    }

    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  onSubmit(): void {
    if (this.form.invalid) { this.form.markAllAsTouched(); return; }

    this.isLoading = true;
    this.authService.login(this.form.value).subscribe({
      next: response => {
        this.isLoading = false;
        if (response.mustChangePassword) {
          this.router.navigate(['/change-password']);
        } else {
          this.router.navigate(['/']);
        }
      },
      error: (err: any) => {
        this.isLoading = false;
        if (err?.error?.errorCode === 'ACCOUNT_BLOCKED') {
          this.router.navigate(['/account-blocked']);
          return;
        }
        if (err?.error?.errorCode === 'MUST_CHANGE_PASSWORD') {
          this.router.navigate(['/change-password']);
          return;
        }
        this.snackBar.open(
          'Identifiants incorrects. Veuillez réessayer.',
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
}
