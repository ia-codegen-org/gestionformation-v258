import { Component }      from '@angular/core';
import { CommonModule }    from '@angular/common';
import { Router }          from '@angular/router';
import { MatIconModule }   from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector:   'app-access-denied',
  standalone:  true,
  imports:    [CommonModule, MatIconModule, MatButtonModule],
  template:   `
    <div class="access-denied">
      <mat-icon>block</mat-icon>
      <h1>Accès refusé</h1>
      <p>Vous n'avez pas les permissions pour accéder à cette page.</p>
      <button mat-raised-button (click)="logout()">
        <mat-icon>logout</mat-icon>
        Se déconnecter
      </button>
    </div>
  `,
  styles: [`
    .access-denied {
      display: flex; flex-direction: column;
      align-items: center; justify-content: center;
      height: 80vh; gap: 16px; text-align: center;
    }
    mat-icon { font-size: 64px; width: 64px; height: 64px; color: #EF4444; }
    h1 { color: #023773; }
    p  { color: #6B7280; }
  `]
})
export class AccessDeniedComponent {
  constructor(private readonly router: Router) {}
  logout(): void {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
