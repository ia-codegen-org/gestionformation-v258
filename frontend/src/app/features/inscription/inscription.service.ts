import { Injectable }     from '@angular/core';
import { HttpClient }     from '@angular/common/http';
import { Observable }     from 'rxjs';
import { environment }    from '../../../environments/environment';
import {
  Inscription,
  InscriptionCreateDTO,
  InscriptionUpdateDTO,
  InscriptionResponseDTO
} from './models/inscription.model';
import { EtudiantResponseDTO } from '../etudiant/models/etudiant.model';
import { SessionResponseDTO } from '../session/models/session.model';
import { PaiementResponseDTO } from '../paiement/models/paiement.model';

@Injectable({ providedIn: 'root' })
export class InscriptionService {

  private readonly apiUrl = `${environment.apiUrl}/api/inscription`;

  constructor(private readonly http: HttpClient) {}

  findAll(): Observable<InscriptionResponseDTO[]> {
    return this.http.get<InscriptionResponseDTO[]>(this.apiUrl);
  }

  count(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }

  statsByStatut(): Observable<{[key: string]: number}> {
    return this.http.get<{[key: string]: number}>(`${this.apiUrl}/stats/by-statut`);
  }

  findById(id: number): Observable<InscriptionResponseDTO> {
    return this.http.get<InscriptionResponseDTO>(`${this.apiUrl}/${id}`);
  }

  create(dto: InscriptionCreateDTO): Observable<InscriptionResponseDTO> {
    return this.http.post<InscriptionResponseDTO>(this.apiUrl, dto);
  }

  update(id: number, dto: InscriptionUpdateDTO): Observable<InscriptionResponseDTO> {
    return this.http.put<InscriptionResponseDTO>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getByEtudiant(etudiantId: number): Observable<InscriptionResponseDTO[]> {
    return this.http.get<InscriptionResponseDTO[]>(`${this.apiUrl}/etudiant/${etudiantId}`);
  }

  getBySession(sessionId: number): Observable<InscriptionResponseDTO[]> {
    return this.http.get<InscriptionResponseDTO[]>(`${this.apiUrl}/session/${sessionId}`);
  }

  getPaiementOfInscription(id: number): Observable<PaiementResponseDTO> {
    return this.http.get<PaiementResponseDTO>(`${this.apiUrl}/${id}/paiement`);
  }
}
