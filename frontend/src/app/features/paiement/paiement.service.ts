import { Injectable }     from '@angular/core';
import { HttpClient }     from '@angular/common/http';
import { Observable }     from 'rxjs';
import { environment }    from '../../../environments/environment';
import {
  Paiement,
  PaiementCreateDTO,
  PaiementUpdateDTO,
  PaiementResponseDTO
} from './models/paiement.model';
import { InscriptionResponseDTO } from '../inscription/models/inscription.model';
import { FactureResponseDTO } from '../facture/models/facture.model';

@Injectable({ providedIn: 'root' })
export class PaiementService {

  private readonly apiUrl = `${environment.apiUrl}/api/paiement`;

  constructor(private readonly http: HttpClient) {}

  findAll(): Observable<PaiementResponseDTO[]> {
    return this.http.get<PaiementResponseDTO[]>(this.apiUrl);
  }

  count(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }

  statsByModePaiement(): Observable<{[key: string]: number}> {
    return this.http.get<{[key: string]: number}>(`${this.apiUrl}/stats/by-modePaiement`);
  }

  findById(id: number): Observable<PaiementResponseDTO> {
    return this.http.get<PaiementResponseDTO>(`${this.apiUrl}/${id}`);
  }

  create(dto: PaiementCreateDTO): Observable<PaiementResponseDTO> {
    return this.http.post<PaiementResponseDTO>(this.apiUrl, dto);
  }

  update(id: number, dto: PaiementUpdateDTO): Observable<PaiementResponseDTO> {
    return this.http.put<PaiementResponseDTO>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getInscriptionOfPaiement(id: number): Observable<InscriptionResponseDTO> {
    return this.http.get<InscriptionResponseDTO>(`${this.apiUrl}/${id}/inscription`);
  }

  getFactureOfPaiement(id: number): Observable<FactureResponseDTO> {
    return this.http.get<FactureResponseDTO>(`${this.apiUrl}/${id}/facture`);
  }
}
