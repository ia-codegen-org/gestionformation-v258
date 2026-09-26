import { HttpInterceptorFn,
         HttpRequest,
         HttpHandlerFn,
         HttpErrorResponse }  from '@angular/common/http';
import { inject }             from '@angular/core';
import { Router }             from '@angular/router';
import { MatSnackBar }        from '@angular/material/snack-bar';
import { catchError }         from 'rxjs/operators';
import { throwError }         from 'rxjs';
import { AuthService }        from '../services/auth.service';

export const errorInterceptor: HttpInterceptorFn = (
    req: HttpRequest<unknown>,
    next: HttpHandlerFn
) => {

  const router      = inject(Router);
  const authService = inject(AuthService);
  const snackBar    = inject(MatSnackBar);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {

      const currentUrl  = router.url;
      const isAuthPage  = currentUrl.includes('/login')
                       || currentUrl.includes('/account-blocked')
                       || currentUrl.includes('/change-password');

      const isSecondary = req.url.includes('/count')
                       || req.url.includes('/stats/')
                       || /\/api\/\w+\/\d+\/\w+/.test(req.url);

      switch (error.status) {

        case 0:
          if (!isAuthPage) {
            snackBar.open('Serveur inaccessible. Vérifiez votre connexion.',
              'Fermer', { duration: 5000, panelClass: ['snack-error'] });
          }
          break;

        case 400:
          snackBar.open(error.error?.message ?? 'Requête invalide.',
            'Fermer', { duration: 4000, panelClass: ['snack-error'] });
          break;

        case 401:
          if (!isAuthPage && !isSecondary) {
            authService.logout();
            snackBar.open('Session expirée. Veuillez vous reconnecter.',
              'Fermer', { duration: 4000, panelClass: ['snack-error'] });
          }
          break;

        case 403:
          if (error.error?.errorCode === 'ACCOUNT_BLOCKED') {
            authService.logout();
            router.navigate(['/account-blocked']);
          } else if (error.error?.errorCode === 'MUST_CHANGE_PASSWORD') {
            router.navigate(['/change-password']);
          } else if (!isSecondary) {
            snackBar.open('Accès refusé.',
              'Fermer', { duration: 3000, panelClass: ['snack-error'] });
          } else {
            console.warn('⚠️ 403 (silencieux):', req.url);
          }
          break;

        case 404:
          console.warn('⚠️ 404:', req.url);
          break;

        case 409:
          snackBar.open(error.error?.message ?? 'Conflit de données.',
            'Fermer', { duration: 4000, panelClass: ['snack-error'] });
          break;

        case 500:
          snackBar.open('Erreur interne du serveur.',
            'Fermer', { duration: 5000, panelClass: ['snack-error'] });
          break;

        default:
          if (!isSecondary) {
            console.warn('⚠️ HTTP Error:', error.status, req.url);
          }
          break;
      }

      return throwError(() => error);
    })
  );
};
