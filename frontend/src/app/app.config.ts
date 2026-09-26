import { ApplicationConfig }                   from '@angular/core';
import { provideRouter }                        from '@angular/router';
import { provideHttpClient,
         withInterceptors }                     from '@angular/common/http';
import { provideAnimationsAsync }               from '@angular/platform-browser/animations/async';
import { MAT_SNACK_BAR_DEFAULT_OPTIONS }        from '@angular/material/snack-bar';
import { routes }                               from './app.routes';
import { authInterceptor }                      from './core/interceptors/auth.interceptor';
import { errorInterceptor }                     from './core/interceptors/error.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
    provideAnimationsAsync(),
    provideHttpClient(
      withInterceptors([authInterceptor,errorInterceptor]) // ✅ PDGC-12 + PDGC-15
    ),
    {
      provide: MAT_SNACK_BAR_DEFAULT_OPTIONS,
      useValue: { verticalPosition: 'top', horizontalPosition: 'right', duration: 3000 }
    }
  ]
};
