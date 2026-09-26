import { Injectable }  from '@angular/core';
import { HttpClient }  from '@angular/common/http';
import { Observable }  from 'rxjs';
import { environment } from '../../../environments/environment';

export interface PermissionEntry {
  id:         number;
  role:       string;
  entityName: string;
  action:     string;
  allowed:    boolean;
  readScope?: string;   // ✅ 'OWN' | 'ALL' — non fourni par le backend actuel (voir PermissionResponseDTO), colonne "Portée" masquée tant qu'absent
}

@Injectable({ providedIn: 'root' })
export class PermissionMatrixService {

  private readonly apiUrl = `${environment.apiUrl}/api/admin/permissions`;

  constructor(private readonly http: HttpClient) {}

  findAll(): Observable<PermissionEntry[]> {
    return this.http.get<PermissionEntry[]>(this.apiUrl);
  }

  updateAllowed(id: number, allowed: boolean): Observable<PermissionEntry> {
    return this.http.put<PermissionEntry>(`${this.apiUrl}/${id}`, { allowed });
  }
}
