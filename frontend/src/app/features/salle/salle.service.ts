import { Injectable }     from '@angular/core';
import { HttpClient }     from '@angular/common/http';
import { Observable }     from 'rxjs';
import { environment }    from '../../../environments/environment';
import {
  Salle,
  SalleCreateDTO,
  SalleUpdateDTO,
  SalleResponseDTO
} from './models/salle.model';
import { BatimentResponseDTO } from '../batiment/models/batiment.model';
import { SessionResponseDTO } from '../session/models/session.model';

@Injectable({ providedIn: 'root' })
export class SalleService {

  private readonly apiUrl = `${environment.apiUrl}/api/salle`;

  constructor(private readonly http: HttpClient) {}

  findAll(): Observable<SalleResponseDTO[]> {
    return this.http.get<SalleResponseDTO[]>(this.apiUrl);
  }

  count(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }

  findById(id: number): Observable<SalleResponseDTO> {
    return this.http.get<SalleResponseDTO>(`${this.apiUrl}/${id}`);
  }

  create(dto: SalleCreateDTO): Observable<SalleResponseDTO> {
    return this.http.post<SalleResponseDTO>(this.apiUrl, dto);
  }

  update(id: number, dto: SalleUpdateDTO): Observable<SalleResponseDTO> {
    return this.http.put<SalleResponseDTO>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getByBatiment(batimentId: number): Observable<SalleResponseDTO[]> {
    return this.http.get<SalleResponseDTO[]>(`${this.apiUrl}/batiment/${batimentId}`);
  }

  getSessionsOfSalle(id: number): Observable<SessionResponseDTO[]> {
    return this.http.get<SessionResponseDTO[]>(`${this.apiUrl}/${id}/sessions`);
  }
}
