import { HttpInterceptorFn,
         HttpRequest,
         HttpHandlerFn }    from '@angular/common/http';
import { inject }          from '@angular/core';
import { Router }          from '@angular/router';
import { AuthService }     from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (
    req: HttpRequest<unknown>,
    next: HttpHandlerFn
) => {

  const authService = inject(AuthService);
  const router       = inject(Router);
  const token       = authService.getToken();

  if (!token || req.url.includes('/auth/login')) {
    return next(req);
  }

  // ✅ Vérifier la validité du token AVANT chaque requête
  if (isTokenExpiredOrInvalid(token)) {
    authService.logout();
    router.navigate(['/login']);
    return next(req); // laisse passer, le backend renverra 401 proprement
  }

  const authReq = req.clone({
    setHeaders: { Authorization: `Bearer ${token}` }
  });

  return next(authReq);
};

function isTokenExpiredOrInvalid(token: string): boolean {
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    if (!payload.exp) return false;
    return Date.now() >= payload.exp * 1000;
  } catch {
    return true; // token malformé → considéré invalide
  }
}
