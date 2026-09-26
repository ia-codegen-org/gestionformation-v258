import { Component } from '@angular/core';
import { CommonModule }       from '@angular/common';
import { Router }             from '@angular/router';
import { MatIconModule }      from '@angular/material/icon';
import { MatButtonModule }    from '@angular/material/button';
import { AuthService }        from '../../../core/services/auth.service';

@Component({
  selector:    'app-blocked-account',
  standalone:  true,
  imports:     [CommonModule, MatIconModule, MatButtonModule],
  templateUrl: './blocked-account.component.html',
  styleUrl:    './blocked-account.component.css'
})
export class BlockedAccountComponent {

  constructor(
    private readonly router:      Router,
    private readonly authService: AuthService
  ) {}

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
