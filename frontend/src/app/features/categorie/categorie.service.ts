import { Injectable }     from '@angular/core';
import { HttpClient }     from '@angular/common/http';
import { Observable }     from 'rxjs';
import { environment }    from '../../../environments/environment';
import {
  Categorie,
  CategorieCreateDTO,
  CategorieUpdateDTO,
  CategorieResponseDTO
} from './models/categorie.model';
import { FormationResponseDTO } from '../formation/models/formation.model';

@Injectable({ providedIn: 'root' })
export class CategorieService {

  private readonly apiUrl = `${environment.apiUrl}/api/categorie`;

  constructor(private readonly http: HttpClient) {}

  findAll(): Observable<CategorieResponseDTO[]> {
    return this.http.get<CategorieResponseDTO[]>(this.apiUrl);
  }

  count(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }

  findById(id: number): Observable<CategorieResponseDTO> {
    return this.http.get<CategorieResponseDTO>(`${this.apiUrl}/${id}`);
  }

  create(dto: CategorieCreateDTO): Observable<CategorieResponseDTO> {
    return this.http.post<CategorieResponseDTO>(this.apiUrl, dto);
  }

  update(id: number, dto: CategorieUpdateDTO): Observable<CategorieResponseDTO> {
    return this.http.put<CategorieResponseDTO>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getFormationsOfCategorie(id: number): Observable<FormationResponseDTO[]> {
    return this.http.get<FormationResponseDTO[]>(`${this.apiUrl}/${id}/formations`);
  }
}
