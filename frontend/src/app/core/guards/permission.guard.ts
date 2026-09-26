import { inject }                         from '@angular/core';
import { CanActivateFn, Router,
         ActivatedRouteSnapshot }             from '@angular/router';
import { map }                                from 'rxjs/operators';
import { PermissionService }                  from '../services/permission.service';

export const permissionGuard: CanActivateFn = (
    route: ActivatedRouteSnapshot
) => {
  const permissionService = inject(PermissionService);
  const router            = inject(Router);

  const entity = route.data['entity'] as string;
  const method = route.data['method'] as 'GET'|'POST'|'PUT'|'DELETE';

  // ─── Pas de data → route publique ou HomeComponent
  if (!entity || !method) return true;

  if (permissionService.isAdmin()) return true;

  return permissionService.loadMyPermissions().pipe(
    map(() => {
      if (permissionService.can(entity, method)) return true;

      const id     = route.params['id'];
      const userId = localStorage.getItem('userId');
      if (id && userId && id === userId) return true;

      // ✅ Rediriger vers access-denied (évite boucle infinie vers '/')
      router.navigate(['/access-denied']);
      return false;
    })
  );
};
