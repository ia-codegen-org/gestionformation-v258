import { Component } from '@angular/core';
import { CommonModule }        from '@angular/common';
import { FormsModule }         from '@angular/forms';
import { Router, RouterLink }  from '@angular/router';
import { HttpClient }          from '@angular/common/http';
import { MatIconModule }          from '@angular/material/icon';
import { MatFormFieldModule }     from '@angular/material/form-field';
import { MatInputModule }         from '@angular/material/input';
import { environment }         from '../../../../environments/environment';

@Component({
  selector:    'app-forgot-password',
  standalone:  true,
  imports:     [CommonModule, FormsModule, RouterLink,
                MatIconModule, MatFormFieldModule, MatInputModule],
  templateUrl: './forgot-password.component.html',
  styleUrl:    './forgot-password.component.css'
})
export class ForgotPasswordComponent {

  private readonly emailPattern = /^[^\s@]{1,64}@[^\s@]{1,255}\.[^\s@]{1,63}$/;

  email       = '';
  isLoading   = false;
  emailSent   = false;
  errorMsg    = '';

  constructor(
    private readonly http:   HttpClient,
    private readonly router: Router
  ) {}

  submit(): void {
    const value = this.email.trim();
    if (!value) return;
    if (!this.emailPattern.test(value)) {
      this.errorMsg = "Format d'email invalide";
      return;
    }
    this.isLoading = true;
    this.errorMsg  = '';

    this.http.post(`${environment.apiUrl}/auth/forgot-password`,
      { email: this.email }
    ).subscribe({
      next: () => {
        this.isLoading = false;
        this.emailSent = true;
      },
      error: () => {
        this.isLoading = false;
        this.emailSent = true;
      }
    });
  }

  backToLogin(): void {
    this.router.navigate(['/login']);
  }
}
