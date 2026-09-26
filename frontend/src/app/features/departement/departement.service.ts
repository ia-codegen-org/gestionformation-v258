import { Injectable }     from '@angular/core';
import { HttpClient }     from '@angular/common/http';
import { Observable }     from 'rxjs';
import { environment }    from '../../../environments/environment';
import {
  Departement,
  DepartementCreateDTO,
  DepartementUpdateDTO,
  DepartementResponseDTO
} from './models/departement.model';
import { FormateurResponseDTO } from '../formateur/models/formateur.model';

@Injectable({ providedIn: 'root' })
export class DepartementService {

  private readonly apiUrl = `${environment.apiUrl}/api/departement`;

  constructor(private readonly http: HttpClient) {}

  findAll(): Observable<DepartementResponseDTO[]> {
    return this.http.get<DepartementResponseDTO[]>(this.apiUrl);
  }

  count(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }

  findById(id: number): Observable<DepartementResponseDTO> {
    return this.http.get<DepartementResponseDTO>(`${this.apiUrl}/${id}`);
  }

  create(dto: DepartementCreateDTO): Observable<DepartementResponseDTO> {
    return this.http.post<DepartementResponseDTO>(this.apiUrl, dto);
  }

  update(id: number, dto: DepartementUpdateDTO): Observable<DepartementResponseDTO> {
    return this.http.put<DepartementResponseDTO>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getFormateursOfDepartement(id: number): Observable<FormateurResponseDTO[]> {
    return this.http.get<FormateurResponseDTO[]>(`${this.apiUrl}/${id}/formateurs`);
  }
}
