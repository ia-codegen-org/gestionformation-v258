import { Injectable }     from '@angular/core';
import { HttpClient }     from '@angular/common/http';
import { Observable }     from 'rxjs';
import { environment }    from '../../../environments/environment';
import {
  Formation,
  FormationCreateDTO,
  FormationUpdateDTO,
  FormationResponseDTO
} from './models/formation.model';
import { CategorieResponseDTO } from '../categorie/models/categorie.model';
import { FormateurResponseDTO } from '../formateur/models/formateur.model';
import { SessionResponseDTO } from '../session/models/session.model';

@Injectable({ providedIn: 'root' })
export class FormationService {

  private readonly apiUrl = `${environment.apiUrl}/api/formation`;

  constructor(private readonly http: HttpClient) {}

  findAll(): Observable<FormationResponseDTO[]> {
    return this.http.get<FormationResponseDTO[]>(this.apiUrl);
  }

  count(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }

  findById(id: number): Observable<FormationResponseDTO> {
    return this.http.get<FormationResponseDTO>(`${this.apiUrl}/${id}`);
  }

  create(dto: FormationCreateDTO): Observable<FormationResponseDTO> {
    return this.http.post<FormationResponseDTO>(this.apiUrl, dto);
  }

  update(id: number, dto: FormationUpdateDTO): Observable<FormationResponseDTO> {
    return this.http.put<FormationResponseDTO>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getByCategorie(categorieId: number): Observable<FormationResponseDTO[]> {
    return this.http.get<FormationResponseDTO[]>(`${this.apiUrl}/categorie/${categorieId}`);
  }

  getFormateursOfFormation(id: number): Observable<FormateurResponseDTO[]> {
    return this.http.get<FormateurResponseDTO[]>(`${this.apiUrl}/${id}/formateurs`);
  }

  addFormateurToFormation(entityId: number, targetId: number): Observable<void> {
    return this.http.post<void>(
      `${environment.apiUrl}/api/formateur/${targetId}/formations/${entityId}`, {});
  }

  removeFormateurFromFormation(entityId: number, targetId: number): Observable<void> {
    return this.http.delete<void>(
      `${environment.apiUrl}/api/formateur/${targetId}/formations/${entityId}`);
  }

  getSessionsOfFormation(id: number): Observable<SessionResponseDTO[]> {
    return this.http.get<SessionResponseDTO[]>(`${this.apiUrl}/${id}/sessions`);
  }
}
