import { Injectable }     from '@angular/core';
import { HttpClient }     from '@angular/common/http';
import { Observable }     from 'rxjs';
import { environment }    from '../../../environments/environment';
import {
  FormateurInterne,
  FormateurInterneCreateDTO,
  FormateurInterneUpdateDTO,
  FormateurInterneResponseDTO
} from './models/formateurinterne.model';

@Injectable({ providedIn: 'root' })
export class FormateurInterneService {

  private readonly apiUrl = `${environment.apiUrl}/api/formateurinterne`;

  constructor(private readonly http: HttpClient) {}

  findAll(): Observable<FormateurInterneResponseDTO[]> {
    return this.http.get<FormateurInterneResponseDTO[]>(this.apiUrl);
  }

  count(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }

  findById(id: number): Observable<FormateurInterneResponseDTO> {
    return this.http.get<FormateurInterneResponseDTO>(`${this.apiUrl}/${id}`);
  }

  create(dto: FormateurInterneCreateDTO): Observable<FormateurInterneResponseDTO> {
    return this.http.post<FormateurInterneResponseDTO>(this.apiUrl, dto);
  }

  update(id: number, dto: FormateurInterneUpdateDTO): Observable<FormateurInterneResponseDTO> {
    return this.http.put<FormateurInterneResponseDTO>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
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
