import { Component, OnInit } from '@angular/core';
import { CommonModule }           from '@angular/common';
import { FormsModule }            from '@angular/forms';
import { Router, ActivatedRoute,
         RouterLink }             from '@angular/router';
import { HttpClient }             from '@angular/common/http';
import { MatIconModule }          from '@angular/material/icon';
import { MatFormFieldModule }     from '@angular/material/form-field';
import { MatInputModule }         from '@angular/material/input';
import { MatButtonModule }        from '@angular/material/button';
import { environment }            from '../../../../environments/environment';

@Component({
  selector:    'app-reset-password',
  standalone:  true,
  imports:     [CommonModule, FormsModule, RouterLink,
                MatIconModule, MatFormFieldModule, MatInputModule,
                MatButtonModule],
  templateUrl: './reset-password.component.html',
  styleUrl:    './reset-password.component.css'
})
export class ResetPasswordComponent implements OnInit {

  token           = '';
  newPassword     = '';
  confirmPassword = '';
  isLoading       = false;
  success         = false;
  errorMsg        = '';
  tokenMissing    = false;
  hideNewPassword     = true;
  hideConfirmPassword = true;

  passwordRules = [
    { label: 'Au moins 8 caractères',         check: (v: string) => v.length >= 8 },
    { label: 'Au moins une majuscule (A-Z)',   check: (v: string) => /[A-Z]/.test(v) },
    { label: 'Au moins une minuscule (a-z)',   check: (v: string) => /[a-z]/.test(v) },
    { label: 'Au moins un chiffre (0-9)',      check: (v: string) => /[0-9]/.test(v) },
    { label: 'Au moins un symbole (!@#$%...)', check: (v: string) => /[!@#$%^&*()_+\-=\[\]{};:\'"|,.<>\/?]/.test(v) }
  ];

  constructor(
    private readonly http:   HttpClient,
    private readonly router: Router,
    private readonly route:  ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token') ?? '';
    if (!this.token) this.tokenMissing = true;
  }

  isPasswordValid(): boolean {
    return this.passwordRules.every(r => r.check(this.newPassword));
  }

  submit(): void {
    this.errorMsg = '';

    if (!this.isPasswordValid()) {
      this.errorMsg = 'Le mot de passe ne respecte pas toutes les règles.';
      return;
    }
    if (this.newPassword !== this.confirmPassword) {
      this.errorMsg = 'Les mots de passe ne correspondent pas.';
      return;
    }

    this.isLoading = true;
    this.http.post(`${environment.apiUrl}/auth/reset-password`, {
      token: this.token,
      newPassword: this.newPassword
    }).subscribe({
      next: () => {
        this.isLoading = false;
        this.success   = true;
        setTimeout(() => this.router.navigate(['/login']), 2500);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMsg  = err?.error?.message
          ?? 'Ce lien est invalide ou a expiré.';
      }
    });
  }
}
