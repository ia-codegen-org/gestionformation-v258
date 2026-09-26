import { Injectable }     from '@angular/core';
import { HttpClient }     from '@angular/common/http';
import { Observable }     from 'rxjs';
import { environment }    from '../../../environments/environment';
import {
  Certificat,
  CertificatCreateDTO,
  CertificatUpdateDTO,
  CertificatResponseDTO
} from './models/certificat.model';
import { EtudiantResponseDTO } from '../etudiant/models/etudiant.model';

@Injectable({ providedIn: 'root' })
export class CertificatService {

  private readonly apiUrl = `${environment.apiUrl}/api/certificat`;

  constructor(private readonly http: HttpClient) {}

  findAll(): Observable<CertificatResponseDTO[]> {
    return this.http.get<CertificatResponseDTO[]>(this.apiUrl);
  }

  count(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }

  statsByMention(): Observable<{[key: string]: number}> {
    return this.http.get<{[key: string]: number}>(`${this.apiUrl}/stats/by-mention`);
  }

  findById(id: number): Observable<CertificatResponseDTO> {
    return this.http.get<CertificatResponseDTO>(`${this.apiUrl}/${id}`);
  }

  create(dto: CertificatCreateDTO): Observable<CertificatResponseDTO> {
    return this.http.post<CertificatResponseDTO>(this.apiUrl, dto);
  }

  update(id: number, dto: CertificatUpdateDTO): Observable<CertificatResponseDTO> {
    return this.http.put<CertificatResponseDTO>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getByEtudiant(etudiantId: number): Observable<CertificatResponseDTO[]> {
    return this.http.get<CertificatResponseDTO[]>(`${this.apiUrl}/etudiant/${etudiantId}`);
  }
}
