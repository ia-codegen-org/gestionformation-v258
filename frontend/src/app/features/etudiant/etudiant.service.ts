import { Injectable }     from '@angular/core';
import { HttpClient }     from '@angular/common/http';
import { Observable }     from 'rxjs';
import { environment }    from '../../../environments/environment';
import {
  Etudiant,
  EtudiantCreateDTO,
  EtudiantUpdateDTO,
  EtudiantResponseDTO
} from './models/etudiant.model';
import { SessionResponseDTO } from '../session/models/session.model';
import { EvaluationResponseDTO } from '../evaluation/models/evaluation.model';
import { CertificatResponseDTO } from '../certificat/models/certificat.model';
import { InscriptionResponseDTO } from '../inscription/models/inscription.model';

@Injectable({ providedIn: 'root' })
export class EtudiantService {

  private readonly apiUrl = `${environment.apiUrl}/api/etudiant`;

  constructor(private readonly http: HttpClient) {}

  findAll(): Observable<EtudiantResponseDTO[]> {
    return this.http.get<EtudiantResponseDTO[]>(this.apiUrl);
  }

  count(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }

  findById(id: number): Observable<EtudiantResponseDTO> {
    return this.http.get<EtudiantResponseDTO>(`${this.apiUrl}/${id}`);
  }

  create(dto: EtudiantCreateDTO): Observable<EtudiantResponseDTO> {
    return this.http.post<EtudiantResponseDTO>(this.apiUrl, dto);
  }

  update(id: number, dto: EtudiantUpdateDTO): Observable<EtudiantResponseDTO> {
    return this.http.put<EtudiantResponseDTO>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getSessionsOfEtudiant(id: number): Observable<SessionResponseDTO[]> {
    return this.http.get<SessionResponseDTO[]>(`${this.apiUrl}/${id}/sessions`);
  }

  addSessionToEtudiant(entityId: number, targetId: number): Observable<void> {
    return this.http.post<void>(
      `${this.apiUrl}/${entityId}/sessions/${targetId}`, {});
  }

  removeSessionFromEtudiant(entityId: number, targetId: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${entityId}/sessions/${targetId}`);
  }

  getEvaluationsOfEtudiant(id: number): Observable<EvaluationResponseDTO[]> {
    return this.http.get<EvaluationResponseDTO[]>(`${this.apiUrl}/${id}/evaluations`);
  }

  getCertificatsOfEtudiant(id: number): Observable<CertificatResponseDTO[]> {
    return this.http.get<CertificatResponseDTO[]>(`${this.apiUrl}/${id}/certificats`);
  }

  getInscriptionsOfEtudiant(id: number): Observable<InscriptionResponseDTO[]> {
    return this.http.get<InscriptionResponseDTO[]>(`${this.apiUrl}/${id}/inscriptions`);
  }

  blockUser(id: number): Observable<void> {
    return this.http.patch<void>(
      `${environment.apiUrl}/auth/users/${id}/block`, {});
  }

  unblockUser(id: number): Observable<void> {
    return this.http.patch<void>(
      `${environment.apiUrl}/auth/users/${id}/unblock`, {});
  }
}
