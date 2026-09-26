import { Injectable }     from '@angular/core';
import { HttpClient }     from '@angular/common/http';
import { Observable }     from 'rxjs';
import { environment }    from '../../../environments/environment';
import {
  Facture,
  FactureCreateDTO,
  FactureUpdateDTO,
  FactureResponseDTO
} from './models/facture.model';
import { PaiementResponseDTO } from '../paiement/models/paiement.model';

@Injectable({ providedIn: 'root' })
export class FactureService {

  private readonly apiUrl = `${environment.apiUrl}/api/facture`;

  constructor(private readonly http: HttpClient) {}

  findAll(): Observable<FactureResponseDTO[]> {
    return this.http.get<FactureResponseDTO[]>(this.apiUrl);
  }

  count(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }

  statsByStatut(): Observable<{[key: string]: number}> {
    return this.http.get<{[key: string]: number}>(`${this.apiUrl}/stats/by-statut`);
  }

  findById(id: number): Observable<FactureResponseDTO> {
    return this.http.get<FactureResponseDTO>(`${this.apiUrl}/${id}`);
  }

  create(dto: FactureCreateDTO): Observable<FactureResponseDTO> {
    return this.http.post<FactureResponseDTO>(this.apiUrl, dto);
  }

  update(id: number, dto: FactureUpdateDTO): Observable<FactureResponseDTO> {
    return this.http.put<FactureResponseDTO>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getPaiementOfFacture(id: number): Observable<PaiementResponseDTO> {
    return this.http.get<PaiementResponseDTO>(`${this.apiUrl}/${id}/paiement`);
  }
}
