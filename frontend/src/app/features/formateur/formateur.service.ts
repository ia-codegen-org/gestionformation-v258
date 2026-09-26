import { Injectable }     from '@angular/core';
import { HttpClient }     from '@angular/common/http';
import { Observable }     from 'rxjs';
import { environment }    from '../../../environments/environment';
import {
  Formateur,
  FormateurCreateDTO,
  FormateurUpdateDTO,
  FormateurResponseDTO
} from './models/formateur.model';
import { DepartementResponseDTO } from '../departement/models/departement.model';
import { FormationResponseDTO } from '../formation/models/formation.model';
import { SessionResponseDTO } from '../session/models/session.model';

@Injectable({ providedIn: 'root' })
export class FormateurService {

  private readonly apiUrl = `${environment.apiUrl}/api/formateur`;

  constructor(private readonly http: HttpClient) {}

  findAll(): Observable<FormateurResponseDTO[]> {
    return this.http.get<FormateurResponseDTO[]>(this.apiUrl);
  }

  count(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }

  findById(id: number): Observable<FormateurResponseDTO> {
    return this.http.get<FormateurResponseDTO>(`${this.apiUrl}/${id}`);
  }

  create(dto: FormateurCreateDTO): Observable<FormateurResponseDTO> {
    return this.http.post<FormateurResponseDTO>(this.apiUrl, dto);
  }

  update(id: number, dto: FormateurUpdateDTO): Observable<FormateurResponseDTO> {
    return this.http.put<FormateurResponseDTO>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getByDepartement(departementId: number): Observable<FormateurResponseDTO[]> {
    return this.http.get<FormateurResponseDTO[]>(`${this.apiUrl}/departement/${departementId}`);
  }

  getFormationsOfFormateur(id: number): Observable<FormationResponseDTO[]> {
    return this.http.get<FormationResponseDTO[]>(`${this.apiUrl}/${id}/formations`);
  }

  addFormationToFormateur(entityId: number, targetId: number): Observable<void> {
    return this.http.post<void>(
      `${this.apiUrl}/${entityId}/formations/${targetId}`, {});
  }

  removeFormationFromFormateur(entityId: number, targetId: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/${entityId}/formations/${targetId}`);
  }

  getSessionsOfFormateur(id: number): Observable<SessionResponseDTO[]> {
    return this.http.get<SessionResponseDTO[]>(`${this.apiUrl}/${id}/sessions`);
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
