import { Injectable }     from '@angular/core';
import { HttpClient }     from '@angular/common/http';
import { Observable }     from 'rxjs';
import { environment }    from '../../../environments/environment';
import {
  Session,
  SessionCreateDTO,
  SessionUpdateDTO,
  SessionResponseDTO
} from './models/session.model';
import { FormationResponseDTO } from '../formation/models/formation.model';
import { FormateurResponseDTO } from '../formateur/models/formateur.model';
import { SalleResponseDTO } from '../salle/models/salle.model';
import { EtudiantResponseDTO } from '../etudiant/models/etudiant.model';
import { InscriptionResponseDTO } from '../inscription/models/inscription.model';
import { EvaluationResponseDTO } from '../evaluation/models/evaluation.model';

@Injectable({ providedIn: 'root' })
export class SessionService {

  private readonly apiUrl = `${environment.apiUrl}/api/session`;

  constructor(private readonly http: HttpClient) {}

  findAll(): Observable<SessionResponseDTO[]> {
    return this.http.get<SessionResponseDTO[]>(this.apiUrl);
  }

  count(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }

  findById(id: number): Observable<SessionResponseDTO> {
    return this.http.get<SessionResponseDTO>(`${this.apiUrl}/${id}`);
  }

  create(dto: SessionCreateDTO): Observable<SessionResponseDTO> {
    return this.http.post<SessionResponseDTO>(this.apiUrl, dto);
  }

  update(id: number, dto: SessionUpdateDTO): Observable<SessionResponseDTO> {
    return this.http.put<SessionResponseDTO>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getByFormation(formationId: number): Observable<SessionResponseDTO[]> {
    return this.http.get<SessionResponseDTO[]>(`${this.apiUrl}/formation/${formationId}`);
  }

  getByFormateur(formateurId: number): Observable<SessionResponseDTO[]> {
    return this.http.get<SessionResponseDTO[]>(`${this.apiUrl}/formateur/${formateurId}`);
  }

  getBySalle(salleId: number): Observable<SessionResponseDTO[]> {
    return this.http.get<SessionResponseDTO[]>(`${this.apiUrl}/salle/${salleId}`);
  }

  getEtudiantsOfSession(id: number): Observable<EtudiantResponseDTO[]> {
    return this.http.get<EtudiantResponseDTO[]>(`${this.apiUrl}/${id}/etudiants`);
  }

  addEtudiantToSession(entityId: number, targetId: number): Observable<void> {
    return this.http.post<void>(
      `${environment.apiUrl}/api/etudiant/${targetId}/sessions/${entityId}`, {});
  }

  removeEtudiantFromSession(entityId: number, targetId: number): Observable<void> {
    return this.http.delete<void>(
      `${environment.apiUrl}/api/etudiant/${targetId}/sessions/${entityId}`);
  }

  getInscriptionsOfSession(id: number): Observable<InscriptionResponseDTO[]> {
    return this.http.get<InscriptionResponseDTO[]>(`${this.apiUrl}/${id}/inscriptions`);
  }

  getEvaluationsOfSession(id: number): Observable<EvaluationResponseDTO[]> {
    return this.http.get<EvaluationResponseDTO[]>(`${this.apiUrl}/${id}/evaluations`);
  }
}
