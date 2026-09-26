import { inject }       from '@angular/core';
import { Router,
         CanActivateFn }  from '@angular/router';
import { AuthService }    from '../services/auth.service';

export const authGuard: CanActivateFn = () => {

  const authService = inject(AuthService);
  const router      = inject(Router);

  if (authService.isLoggedIn()) {
    return true; // ✅ connecté → accès autorisé
  }

  // ─── Pas connecté → redirect login
  router.navigate(['/login']);
  return false;
};
