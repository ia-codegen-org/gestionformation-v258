import { Component, OnInit } from '@angular/core';
import { CommonModule }           from '@angular/common';
import { MatIconModule }          from '@angular/material/icon';
import { MatButtonModule }        from '@angular/material/button';
import { Router }                 from '@angular/router';
import { FormateurService } from '../formateur/formateur.service';
import { FormateurInterneService } from '../formateurinterne/formateurinterne.service';
import { FormateurExterneService } from '../formateurexterne/formateurexterne.service';
import { EtudiantService } from '../etudiant/etudiant.service';

@Component({
  selector:    'app-profile',
  standalone:  true,
  imports:     [CommonModule, MatIconModule, MatButtonModule],
  templateUrl: './profile.component.html',
  styleUrl:    './profile.component.css'
})
export class ProfileComponent implements OnInit {

  username   = '';
  email      = '';
  role       = '';
  userId     = '';
  fields:     { label: string; value: string }[]                       = [];
  actorStats: { label: string; count: number; icon: string; color: string }[] = [];

  constructor(
    private readonly formateurService: FormateurService,
    private readonly formateurinterneService: FormateurInterneService,
    private readonly formateurexterneService: FormateurExterneService,
    private readonly etudiantService: EtudiantService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.username = localStorage.getItem('username') ?? '';
    this.email    = localStorage.getItem('email')    ?? '';
    this.role     = localStorage.getItem('role')     ?? '';
    this.userId   = localStorage.getItem('userId')   ?? '';

    this.fields = [
      { label: "Nom d'utilisateur", value: this.username },
      { label: 'Adresse email',     value: this.email    },
      { label: 'Rôle',              value: this.role     },
    ].filter(f => f.value !== '');

    // ✅ Stats calculées asynchrones — erreurs silencieuses
    if (this.userId) {
      const role = this.role.trim().toUpperCase();
      switch (role) {
      case 'FORMATEUR':
        this.formateurService
          .getFormationsOfFormateur(+this.userId).subscribe({
            next: (list: any[]) => {
              this.actorStats.push({
                label: 'Formations',
                count: list.length,
                icon:  'article',
                color: '#64748B'
              });
            },
            error: () => {}
          });
        this.formateurService
          .getSessionsOfFormateur(+this.userId).subscribe({
            next: (list: any[]) => {
              this.actorStats.push({
                label: 'Sessions',
                count: list.length,
                icon:  'event',
                color: '#7C3AED'
              });
            },
            error: () => {}
          });
        break;
      case 'ETUDIANT':
        this.etudiantService
          .getSessionsOfEtudiant(+this.userId).subscribe({
            next: (list: any[]) => {
              this.actorStats.push({
                label: 'Sessions',
                count: list.length,
                icon:  'event',
                color: '#7C3AED'
              });
            },
            error: () => {}
          });
        this.etudiantService
          .getEvaluationsOfEtudiant(+this.userId).subscribe({
            next: (list: any[]) => {
              this.actorStats.push({
                label: 'Evaluations',
                count: list.length,
                icon:  'grade',
                color: '#DB2777'
              });
              const _noteTotal = list.reduce(
                (sum: number, item: any) => sum + (item.note ?? 0), 0);
              if (_noteTotal > 0) {
                this.actorStats.push({
                  label: 'Total Note',
                  count: Math.round(_noteTotal),
                  icon:  'payments',
                  color: '#059669'
                });
              }
            },
            error: () => {}
          });
        this.etudiantService
          .getCertificatsOfEtudiant(+this.userId).subscribe({
            next: (list: any[]) => {
              this.actorStats.push({
                label: 'Certificats',
                count: list.length,
                icon:  'event_note',
                color: '#7C3AED'
              });
            },
            error: () => {}
          });
        this.etudiantService
          .getInscriptionsOfEtudiant(+this.userId).subscribe({
            next: (list: any[]) => {
              this.actorStats.push({
                label: 'Inscriptions',
                count: list.length,
                icon:  'assignment_turned_in',
                color: '#7C3AED'
              });
            },
            error: () => {}
          });
        break;
        default: break;
      }
    }
  }

  private readonly editableRoles: string[] = ['FORMATEUR', 'FORMATEURINTERNE', 'FORMATEUREXTERNE', 'ETUDIANT'];

  canEditProfile(): boolean {
    return this.editableRoles.includes(this.role.toUpperCase());
  }

  editProfile(): void {
    const role = this.role.trim().toUpperCase();
    switch (role) {
      case 'FORMATEUR':
        this.router.navigate(['/formateur/edit', this.userId]);
        break;
      case 'FORMATEURINTERNE':
        this.router.navigate(['/formateurinterne/edit', this.userId]);
        break;
      case 'FORMATEUREXTERNE':
        this.router.navigate(['/formateurexterne/edit', this.userId]);
        break;
      case 'ETUDIANT':
        this.router.navigate(['/etudiant/edit', this.userId]);
        break;
      default: break;
    }
  }
}
