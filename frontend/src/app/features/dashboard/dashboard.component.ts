import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule }                           from '@angular/common';
import { Router }                                 from '@angular/router';
import { MatIconModule }                          from '@angular/material/icon';
import { PermissionService }                      from '../../core/services/permission.service';
import { Chart, registerables }                   from 'chart.js';
import { FormateurService } from '../formateur/formateur.service';
import { FormateurInterneService } from '../formateurinterne/formateurinterne.service';
import { FormateurExterneService } from '../formateurexterne/formateurexterne.service';
import { EtudiantService } from '../etudiant/etudiant.service';
import { FormationService } from '../formation/formation.service';
import { SessionService } from '../session/session.service';
import { SalleService } from '../salle/salle.service';
import { BatimentService } from '../batiment/batiment.service';
import { DepartementService } from '../departement/departement.service';
import { CategorieService } from '../categorie/categorie.service';
import { InscriptionService } from '../inscription/inscription.service';
import { EvaluationService } from '../evaluation/evaluation.service';
import { CertificatService } from '../certificat/certificat.service';
import { PaiementService } from '../paiement/paiement.service';
import { FactureService } from '../facture/facture.service';

Chart.register(...registerables);

interface SubStat     { label: string; value: number; color: string; }
interface StatCard    { entity: string; label: string; icon: string; count: number; route: string; color: string; subStats: SubStat[]; }
interface EnumValStat { label: string; count: number; color: string; bg: string; percent: number; }
interface EnumSection { title: string; chartId: string; stats: EnumValStat[]; total: number; }

@Component({
  selector:    'app-dashboard',
  standalone:  true,
  imports:     [CommonModule, MatIconModule],
  templateUrl: './dashboard.component.html',
  styleUrl:    './dashboard.component.css'
})
export class DashboardComponent implements OnInit, OnDestroy {

  username = localStorage.getItem('username') ?? '';
  role     = localStorage.getItem('role')     ?? '';

  formateurCount = 0;
  formateurActiveCount   = 0;
  formateurInactiveCount = 0;
  formateurinterneCount = 0;
  formateurinterneActiveCount   = 0;
  formateurinterneInactiveCount = 0;
  formateurexterneCount = 0;
  formateurexterneActiveCount   = 0;
  formateurexterneInactiveCount = 0;
  etudiantCount = 0;
  etudiantActiveCount   = 0;
  etudiantInactiveCount = 0;
  formationCount = 0;
  sessionCount = 0;
  salleCount = 0;
  batimentCount = 0;
  departementCount = 0;
  categorieCount = 0;
  inscriptionCount = 0;
  evaluationCount = 0;
  certificatCount = 0;
  paiementCount = 0;
  factureCount = 0;

  allCards:     StatCard[]    = [];
  visibleCards: StatCard[]    = [];
  enumSections: EnumSection[] = [];

  private readonly enumPalette = [
    { color: '#3B82F6', bg: '#EFF6FF' },
    { color: '#10B981', bg: '#ECFDF5' },
    { color: '#F59E0B', bg: '#FFFBEB' },
    { color: '#EF4444', bg: '#FEF2F2' },
    { color: '#8B5CF6', bg: '#F5F3FF' },
    { color: '#EC4899', bg: '#FDF2F8' },
    { color: '#14B8A6', bg: '#F0FDFA' },
    { color: '#F97316', bg: '#FFF7ED' },
    { color: '#6366F1', bg: '#EEF2FF' },
    { color: '#84CC16', bg: '#F7FEE7' },
  ];

  private chartInstances = new Map<string, Chart>();
  // ✅ Handles des setTimeout d'initChart en attente — sans ça, un
  // setTimeout planifié juste avant une navigation hors de ce composant
  // s'exécute quand même plus tard (setTimeout n'est pas lié au cycle de
  // vie Angular), et Chart.js/ResizeObserver s'accumulent silencieusement
  // à chaque visite du Dashboard jusqu'à saturer le thread principal.
  private chartTimeouts: ReturnType<typeof setTimeout>[] = [];

  constructor(
    private readonly router:            Router,
    readonly         permissionService: PermissionService,
    private readonly formateurService: FormateurService,
    private readonly formateurinterneService: FormateurInterneService,
    private readonly formateurexterneService: FormateurExterneService,
    private readonly etudiantService: EtudiantService,
    private readonly formationService: FormationService,
    private readonly sessionService: SessionService,
    private readonly salleService: SalleService,
    private readonly batimentService: BatimentService,
    private readonly departementService: DepartementService,
    private readonly categorieService: CategorieService,
    private readonly inscriptionService: InscriptionService,
    private readonly evaluationService: EvaluationService,
    private readonly certificatService: CertificatService,
    private readonly paiementService: PaiementService,
    private readonly factureService: FactureService
  ) {}

  ngOnInit(): void {
    let loaded = 0;
    const total: number = 15;
    const done = () => { if (++loaded >= total) this.buildCards(); };

    this.buildCards();

    // ─── Formateur ───
    this.loadEntityStats(
      'Formateur',
      () => this.formateurService.count(),
      (n: number) => this.formateurCount = n,
      done,
      {
        loadActive: () => this.formateurService.findAll(),
        setActiveCounts: (active: number, inactive: number) => {
          this.formateurActiveCount   = active;
          this.formateurInactiveCount = inactive;
        },
      }
    );

    // ─── FormateurInterne ───
    this.loadEntityStats(
      'FormateurInterne',
      () => this.formateurinterneService.count(),
      (n: number) => this.formateurinterneCount = n,
      done,
      {
        loadActive: () => this.formateurinterneService.findAll(),
        setActiveCounts: (active: number, inactive: number) => {
          this.formateurinterneActiveCount   = active;
          this.formateurinterneInactiveCount = inactive;
        },
      }
    );

    // ─── FormateurExterne ───
    this.loadEntityStats(
      'FormateurExterne',
      () => this.formateurexterneService.count(),
      (n: number) => this.formateurexterneCount = n,
      done,
      {
        loadActive: () => this.formateurexterneService.findAll(),
        setActiveCounts: (active: number, inactive: number) => {
          this.formateurexterneActiveCount   = active;
          this.formateurexterneInactiveCount = inactive;
        },
      }
    );

    // ─── Etudiant ───
    this.loadEntityStats(
      'Etudiant',
      () => this.etudiantService.count(),
      (n: number) => this.etudiantCount = n,
      done,
      {
        loadActive: () => this.etudiantService.findAll(),
        setActiveCounts: (active: number, inactive: number) => {
          this.etudiantActiveCount   = active;
          this.etudiantInactiveCount = inactive;
        },
      }
    );

    // ─── Formation ───
    this.loadEntityStats(
      'Formation',
      () => this.formationService.count(),
      (n: number) => this.formationCount = n,
      done,
      {
      }
    );

    // ─── Session ───
    this.loadEntityStats(
      'Session',
      () => this.sessionService.count(),
      (n: number) => this.sessionCount = n,
      done,
      {
      }
    );

    // ─── Salle ───
    this.loadEntityStats(
      'Salle',
      () => this.salleService.count(),
      (n: number) => this.salleCount = n,
      done,
      {
      }
    );

    // ─── Batiment ───
    this.loadEntityStats(
      'Batiment',
      () => this.batimentService.count(),
      (n: number) => this.batimentCount = n,
      done,
      {
      }
    );

    // ─── Departement ───
    this.loadEntityStats(
      'Departement',
      () => this.departementService.count(),
      (n: number) => this.departementCount = n,
      done,
      {
      }
    );

    // ─── Categorie ───
    this.loadEntityStats(
      'Categorie',
      () => this.categorieService.count(),
      (n: number) => this.categorieCount = n,
      done,
      {
      }
    );

    // ─── Inscription ───
    this.loadEntityStats(
      'Inscription',
      () => this.inscriptionService.count(),
      (n: number) => this.inscriptionCount = n,
      done,
      {
        enumStats: [
          { label: 'Inscriptions par statut', chartId: 'inscription-statut', load: () => this.inscriptionService.statsByStatut() },
        ],
      }
    );

    // ─── Evaluation ───
    this.loadEntityStats(
      'Evaluation',
      () => this.evaluationService.count(),
      (n: number) => this.evaluationCount = n,
      done,
      {
      }
    );

    // ─── Certificat ───
    this.loadEntityStats(
      'Certificat',
      () => this.certificatService.count(),
      (n: number) => this.certificatCount = n,
      done,
      {
        enumStats: [
          { label: 'Certificats par mention', chartId: 'certificat-mention', load: () => this.certificatService.statsByMention() },
        ],
      }
    );

    // ─── Paiement ───
    this.loadEntityStats(
      'Paiement',
      () => this.paiementService.count(),
      (n: number) => this.paiementCount = n,
      done,
      {
        enumStats: [
          { label: 'Paiements par modePaiement', chartId: 'paiement-modePaiement', load: () => this.paiementService.statsByModePaiement() },
        ],
      }
    );

    // ─── Facture ───
    this.loadEntityStats(
      'Facture',
      () => this.factureService.count(),
      (n: number) => this.factureCount = n,
      done,
      {
        enumStats: [
          { label: 'Factures par statut', chartId: 'facture-statut', load: () => this.factureService.statsByStatut() },
        ],
      }
    );

  }

  buildCards(): void {
    this.allCards = [
    {
      entity:   'Formateur',
      label:    'Formateurs',
      icon:     'person',
      count:    this.formateurCount,
      route:    '/formateur',
      color:    '#023773',
      subStats: [
        { label: 'Actifs',   value: this.formateurActiveCount,   color: '#22C55E' },
        { label: 'Inactifs', value: this.formateurInactiveCount, color: '#EF4444' }
      ]
    },
    {
      entity:   'FormateurInterne',
      label:    'FormateurInternes',
      icon:     'person',
      count:    this.formateurinterneCount,
      route:    '/formateurinterne',
      color:    '#023773',
      subStats: [
        { label: 'Actifs',   value: this.formateurinterneActiveCount,   color: '#22C55E' },
        { label: 'Inactifs', value: this.formateurinterneInactiveCount, color: '#EF4444' }
      ]
    },
    {
      entity:   'FormateurExterne',
      label:    'FormateurExternes',
      icon:     'person',
      count:    this.formateurexterneCount,
      route:    '/formateurexterne',
      color:    '#023773',
      subStats: [
        { label: 'Actifs',   value: this.formateurexterneActiveCount,   color: '#22C55E' },
        { label: 'Inactifs', value: this.formateurexterneInactiveCount, color: '#EF4444' }
      ]
    },
    {
      entity:   'Etudiant',
      label:    'Etudiants',
      icon:     'person',
      count:    this.etudiantCount,
      route:    '/etudiant',
      color:    '#023773',
      subStats: [
        { label: 'Actifs',   value: this.etudiantActiveCount,   color: '#22C55E' },
        { label: 'Inactifs', value: this.etudiantInactiveCount, color: '#EF4444' }
      ]
    },
    {
      entity:   'Formation',
      label:    'Formations',
      icon:     'article',
      count:    this.formationCount,
      route:    '/formation',
      color:    '#64748B',
      subStats: []
    },
    {
      entity:   'Session',
      label:    'Sessions',
      icon:     'event',
      count:    this.sessionCount,
      route:    '/session',
      color:    '#7C3AED',
      subStats: []
    },
    {
      entity:   'Salle',
      label:    'Salles',
      icon:     'layers',
      count:    this.salleCount,
      route:    '/salle',
      color:    '#64748B',
      subStats: []
    },
    {
      entity:   'Batiment',
      label:    'Batiments',
      icon:     'location_on',
      count:    this.batimentCount,
      route:    '/batiment',
      color:    '#F59E0B',
      subStats: []
    },
    {
      entity:   'Departement',
      label:    'Departements',
      icon:     'payments',
      count:    this.departementCount,
      route:    '/departement',
      color:    '#0284C7',
      subStats: []
    },
    {
      entity:   'Categorie',
      label:    'Categories',
      icon:     'article',
      count:    this.categorieCount,
      route:    '/categorie',
      color:    '#0284C7',
      subStats: []
    },
    {
      entity:   'Inscription',
      label:    'Inscriptions',
      icon:     'assignment_turned_in',
      count:    this.inscriptionCount,
      route:    '/inscription',
      color:    '#7C3AED',
      subStats: []
    },
    {
      entity:   'Evaluation',
      label:    'Evaluations',
      icon:     'grade',
      count:    this.evaluationCount,
      route:    '/evaluation',
      color:    '#DB2777',
      subStats: []
    },
    {
      entity:   'Certificat',
      label:    'Certificats',
      icon:     'event_note',
      count:    this.certificatCount,
      route:    '/certificat',
      color:    '#7C3AED',
      subStats: []
    },
    {
      entity:   'Paiement',
      label:    'Paiements',
      icon:     'payments',
      count:    this.paiementCount,
      route:    '/paiement',
      color:    '#059669',
      subStats: []
    },
    {
      entity:   'Facture',
      label:    'Factures',
      icon:     'payments',
      count:    this.factureCount,
      route:    '/facture',
      color:    '#059669',
      subStats: []
    },
    ];
    this.visibleCards = this.allCards.filter(c =>
      this.permissionService.can(c.entity, 'GET')
    );
  }

  loadEntityStats(
    entityName: string,
    loadCount: () => any,
    setCount: (n: number) => void,
    done: () => void,
    options?: {
      loadActive?: () => any;
      setActiveCounts?: (active: number, inactive: number) => void;
      enumStats?: { label: string; chartId: string; load: () => any }[];
    }
  ): void {
    if (!this.permissionService.can(entityName, 'GET')) { done(); return; }

    loadCount().subscribe({
      next: (n: number) => {
        setCount(n);

        if (options?.loadActive && this.permissionService.isAdmin()) {
          options.loadActive().subscribe({
            next: (list: any[]) => {
              const active   = list.filter((u: any) => u.enabled !== false).length;
              const inactive = list.filter((u: any) => u.enabled === false).length;
              options?.setActiveCounts?.(active, inactive);
              done();
            },
            error: () => done()
          });
        } else {
          done();
        }

        (options?.enumStats ?? []).forEach(es => {
          es.load().subscribe({
            next: (data: any) => {
              this.addEnumSection(es.label, data, es.chartId);
              this.initChart(es.chartId, es.label, data);
            },
            error: () => {}
          });
        });
      },
      error: () => done()
    });
  }

  addEnumSection(title: string, data: {[key: string]: number}, chartId: string): void {
    const total = Object.values(data).reduce((a, b) => a + b, 0);
    const stats: EnumValStat[] = Object.entries(data).map(([key, val], i) => ({
      label:   key,
      count:   val as number,
      color:   this.enumPalette[i % this.enumPalette.length].color,
      bg:      this.enumPalette[i % this.enumPalette.length].bg,
      percent: total > 0 ? Math.round(((val as number) / total) * 100) : 0,
    }));
    const idx     = this.enumSections.findIndex(s => s.chartId === chartId);
    const section: EnumSection = { title, chartId, stats, total };
    if (idx >= 0) { this.enumSections[idx] = section; }
    else          { this.enumSections.push(section); }
  }

  initChart(id: string, label: string, data: {[key: string]: number}): void {
    const timeout = setTimeout(() => {
      const canvas = document.getElementById('chart-' + id) as HTMLCanvasElement;
      if (!canvas) return;
      const existing = this.chartInstances.get(id);
      if (existing) existing.destroy();
      const labels = Object.keys(data);
      const values = Object.values(data);
      const bgCols = this.enumPalette.map(p => p.color);
      const chart  = new Chart(canvas, {
        type: 'bar',
        data: {
          labels,
          datasets: [{
            label,
            data:            values,
            backgroundColor: labels.map((_, i) => bgCols[i % bgCols.length]),
            borderColor:     labels.map((_, i) => bgCols[i % bgCols.length]),
            borderWidth:     0,
            borderRadius:    10,
            borderSkipped:   false,
          }]
        },
        options: {
          responsive: true, maintainAspectRatio: false,
          plugins: {
            legend: { display: false },
            tooltip: {
              backgroundColor: 'rgba(15,23,42,0.92)',
              titleColor: '#fff', bodyColor: '#CBD5E1',
              padding: 12, cornerRadius: 8,
              callbacks: { label: ctx => ` ${ctx.parsed.y} enregistrement(s)` }
            }
          },
          scales: {
            x: { grid: { display: false },
                 ticks: { color: '#64748B', font: { size: 12 } } },
            y: { beginAtZero: true, grid: { color: '#F1F5F9' },
                 ticks: { precision: 0, color: '#94A3B8', font: { size: 11 } } }
          }
        }
      });
      this.chartInstances.set(id, chart);
    }, 200);
    this.chartTimeouts.push(timeout);
  }

  // ✅ Sans ceci, les instances Chart.js (+ leurs ResizeObserver internes)
  // survivent à la destruction du composant à chaque navigation hors du
  // Dashboard — répété sur plusieurs visites, l'accumulation finit par
  // geler le thread principal (page qui reste bloquée en "chargement"
  // sur la navigation suivante, sans lien avec la page réellement visée).
  ngOnDestroy(): void {
    this.chartTimeouts.forEach(t => clearTimeout(t));
    this.chartInstances.forEach(chart => chart.destroy());
    this.chartInstances.clear();
  }

  navigate(route: string): void { this.router.navigate([route]); }

  getGreeting(): string {
    const h = new Date().getHours();
    if (h < 12) return 'Bonjour';
    if (h < 18) return 'Bon après-midi';
    return 'Bonsoir';
  }
}
