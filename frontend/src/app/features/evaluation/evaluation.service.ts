import { Injectable }     from '@angular/core';
import { HttpClient }     from '@angular/common/http';
import { Observable }     from 'rxjs';
import { environment }    from '../../../environments/environment';
import {
  Evaluation,
  EvaluationCreateDTO,
  EvaluationUpdateDTO,
  EvaluationResponseDTO
} from './models/evaluation.model';
import { EtudiantResponseDTO } from '../etudiant/models/etudiant.model';
import { SessionResponseDTO } from '../session/models/session.model';

@Injectable({ providedIn: 'root' })
export class EvaluationService {

  private readonly apiUrl = `${environment.apiUrl}/api/evaluation`;

  constructor(private readonly http: HttpClient) {}

  findAll(): Observable<EvaluationResponseDTO[]> {
    return this.http.get<EvaluationResponseDTO[]>(this.apiUrl);
  }

  count(): Observable<number> {
    return this.http.get<number>(`${this.apiUrl}/count`);
  }

  findById(id: number): Observable<EvaluationResponseDTO> {
    return this.http.get<EvaluationResponseDTO>(`${this.apiUrl}/${id}`);
  }

  create(dto: EvaluationCreateDTO): Observable<EvaluationResponseDTO> {
    return this.http.post<EvaluationResponseDTO>(this.apiUrl, dto);
  }

  update(id: number, dto: EvaluationUpdateDTO): Observable<EvaluationResponseDTO> {
    return this.http.put<EvaluationResponseDTO>(`${this.apiUrl}/${id}`, dto);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getByEtudiant(etudiantId: number): Observable<EvaluationResponseDTO[]> {
    return this.http.get<EvaluationResponseDTO[]>(`${this.apiUrl}/etudiant/${etudiantId}`);
  }

  getBySession(sessionId: number): Observable<EvaluationResponseDTO[]> {
    return this.http.get<EvaluationResponseDTO[]>(`${this.apiUrl}/session/${sessionId}`);
  }
}
