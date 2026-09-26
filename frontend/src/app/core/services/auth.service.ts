import { Injectable }  from '@angular/core';
import { HttpClient }  from '@angular/common/http';
import { Observable }  from 'rxjs';
import { tap }         from 'rxjs/operators';
import { Router }      from '@angular/router';
import { environment } from '../../../environments/environment';

// ─── DTOs Auth
export interface LoginRequest {
  username: string;
  password: string;
}

export interface ChangePasswordRequest {
  oldPassword: string;
  newPassword: string;
}

export interface AuthResponse {
  token:              string;
  type:               string;
  id:                 number;
  username:           string;
  role:               string;
  mustChangePassword: boolean;
}

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly authUrl = `${environment.apiUrl}/auth`;

  // ─── Clés localStorage
  private readonly TOKEN_KEY    = 'token';
  private readonly USERNAME_KEY = 'username';
  private readonly ROLE_KEY     = 'role';

  constructor(
    private readonly http:   HttpClient,
    private readonly router: Router
  ) {}

  // ─── Login
  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.authUrl}/login`, request)
      .pipe(
        tap(response => {
          // ✅ Stocker le token dans localStorage
          localStorage.setItem(this.TOKEN_KEY,    response.token);
          localStorage.setItem(this.USERNAME_KEY, response.username);
          localStorage.setItem(this.ROLE_KEY,     response.role);

          localStorage.setItem('userId', response.id.toString());
          // ✅ Rediriger selon mustChangePassword
          if (response.mustChangePassword) {
            this.router.navigate(['/change-password']);
          }
        })
      );
  }

  // ─── Change Password
  changePassword(request: ChangePasswordRequest): Observable<void> {
    return this.http
      .patch<void>(`${this.authUrl}/change-password`, request)
      .pipe(
        tap(() => {
          // ✅ Après changement → rediriger vers home
          this.router.navigate(['/']);
        })
      );
  }

  // ─── Logout
  logout(): void {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.USERNAME_KEY);
    localStorage.removeItem(this.ROLE_KEY);
    this.router.navigate(['/login']);
  }

  // ─── Récupérer le token
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  // ─── Récupérer le username
  getUsername(): string | null {
    return localStorage.getItem(this.USERNAME_KEY);
  }

  // ─── Récupérer le rôle
  getRole(): string | null {
    return localStorage.getItem(this.ROLE_KEY);
  }

  // ─── Vérifier si connecté
  isLoggedIn(): boolean {
    return !!localStorage.getItem(this.TOKEN_KEY);
  }

  // ─── Vérifier un rôle
  hasRole(role: string): boolean {
    return this.getRole() === role;
  }
}
