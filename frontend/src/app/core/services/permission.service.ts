import { Injectable }           from '@angular/core';
import { HttpClient }           from '@angular/common/http';
import { Observable }           from 'rxjs';
import { tap }                  from 'rxjs/operators';
import { environment }          from '../../../environments/environment';
import { ASSIGN_PERMISSION }    from '../config/permissions.config';

@Injectable({ providedIn: 'root' })
export class PermissionService {

  private myPermissions: Record<string, Record<string, boolean>> = {};

  constructor(private readonly http: HttpClient) {}

  loadMyPermissions(): Observable<Record<string, Record<string, boolean>>> {
    return this.http
      .get<Record<string, Record<string, boolean>>>(
        `${environment.apiUrl}/api/admin/permissions/mine`)
      .pipe(tap(perms => { this.myPermissions = perms; }));
  }

  getRole():     string  { return localStorage.getItem('role')     ?? ''; }
  getUsername(): string  { return localStorage.getItem('username') ?? ''; }
  getUserId():   string  { return localStorage.getItem('userId')   ?? ''; }
  isAdmin():     boolean { return this.getRole() === 'ADMIN'; }

  can(entity: string,
      method: 'GET' | 'POST' | 'PUT' | 'DELETE'): boolean {
    const role = this.getRole();
    if (role === 'ADMIN') return true;
    return this.myPermissions[entity]?.[method] === true;
  }

  canAssign(): boolean {
    return this.getRole() === ASSIGN_PERMISSION;
  }

  canEditRow(row: any): boolean {
    if (this.isAdmin()) return true;

    const username = this.getUsername();

    if (row.createdBy != null && row.createdBy === username) {
      return true;
    }

    const usernameFields = Object.keys(row)
      .filter(k => k.endsWith('Usernames') && Array.isArray(row[k]));

    if (usernameFields.length > 0) {
      const allAssigned: string[] = usernameFields
        .flatMap(k => row[k] as string[]);

      return allAssigned.includes(username);
    }

    if (row.createdBy == null) {
      return true;
    }

    return row.createdBy === username
        || row.assignedTo  === username;
  }
}
