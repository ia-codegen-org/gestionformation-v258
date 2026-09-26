import { inject }         from '@angular/core';
import { Router,
         CanActivateFn }  from '@angular/router';
import { PermissionService } from '../services/permission.service';

export const adminGuard: CanActivateFn = () => {

  const permissionService = inject(PermissionService);
  const router            = inject(Router);

  if (permissionService.isAdmin()) {
    return true; // ✅ ADMIN → accès autorisé
  }

  // ─── Pas ADMIN → refus
  router.navigate(['/access-denied']);
  return false;
};
