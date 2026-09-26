import { Component }      from '@angular/core';
import { CommonModule }    from '@angular/common';
import { Router }          from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule }   from '@angular/material/icon';

@Component({
  selector:    'app-not-found',
  standalone:  true,
  imports:     [CommonModule, MatButtonModule, MatIconModule],
  templateUrl: './not-found.component.html',
  styleUrl:    './not-found.component.css'
})
export class NotFoundComponent {
  constructor(private readonly router: Router) {}
  goHome(): void { this.router.navigate(['/']); }
}
