import { Injectable }     from '@angular/core';
import { HttpClient }     from '@angular/common/http';
import { Observable }     from 'rxjs';
import { environment }    from '../../../environments/environment';
import {
  Batiment,
  BatimentCreateDTO,
  BatimentUpdateDTO,
  BatimentResponseDTO
} from './models/batiment.model';
import { SalleResponseDTO } from '../salle/models/salle.model';

@Injectable({ providedIn: 'root' })
export class BatimentService {

  private readonly apiUrl = `${environment.apiUrl}/api/batiment`;

  constructor(private readonly http: HttpClient) {}

  findAll(): Observable<BatimentResponseDTO[]> {
    return this.http.get<BatimentResponseDTO[]>(this.apiUrl);
  }

  count(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }

  findById(id: number): Observable<BatimentResponseDTO> {
    return this.http.get<BatimentResponseDTO>(`${this.apiUrl}/${id}`);
  }

  create(dto: BatimentCreateDTO): Observable<BatimentResponseDTO> {
    return this.http.post<BatimentResponseDTO>(this.apiUrl, dto);
  }

  update(id: number, dto: BatimentUpdateDTO): Observable<BatimentResponseDTO> {
    return this.http.put<BatimentResponseDTO>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getSallesOfBatiment(id: number): Observable<SalleResponseDTO[]> {
    return this.http.get<SalleResponseDTO[]>(`${this.apiUrl}/${id}/salles`);
  }
}
