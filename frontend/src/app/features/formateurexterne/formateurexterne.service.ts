import { Injectable }     from '@angular/core';
import { HttpClient }     from '@angular/common/http';
import { Observable }     from 'rxjs';
import { environment }    from '../../../environments/environment';
import {
  FormateurExterne,
  FormateurExterneCreateDTO,
  FormateurExterneUpdateDTO,
  FormateurExterneResponseDTO
} from './models/formateurexterne.model';

@Injectable({ providedIn: 'root' })
export class FormateurExterneService {

  private readonly apiUrl = `${environment.apiUrl}/api/formateurexterne`;

  constructor(private readonly http: HttpClient) {}

  findAll(): Observable<FormateurExterneResponseDTO[]> {
    return this.http.get<FormateurExterneResponseDTO[]>(this.apiUrl);
  }

  count(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }

  findById(id: number): Observable<FormateurExterneResponseDTO> {
    return this.http.get<FormateurExterneResponseDTO>(`${this.apiUrl}/${id}`);
  }

  create(dto: FormateurExterneCreateDTO): Observable<FormateurExterneResponseDTO> {
    return this.http.post<FormateurExterneResponseDTO>(this.apiUrl, dto);
  }

  update(id: number, dto: FormateurExterneUpdateDTO): Observable<FormateurExterneResponseDTO> {
    return this.http.put<FormateurExterneResponseDTO>(`${this.apiUrl}/${id}`, dto);
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
