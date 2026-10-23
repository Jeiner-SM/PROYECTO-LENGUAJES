import { ApplicationConfig, provideBrowserGlobalErrorListeners, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';
import { providePrimeNG } from 'primeng/config';
import { provideAnimations } from '@angular/platform-browser/animations';
import { ConfirmationService } from 'primeng/api';


import { routes } from './app.routes';
import Aura from '@primeuix/themes/aura';
import { provideHttpClient, withInterceptors } from '@angular/common/http';

import { MessageService } from 'primeng/api';
import { authInterceptor } from './core/interceptors/auth-interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideZoneChangeDetection({ eventCoalescing: true }),
    provideAnimations(),
    provideRouter(routes),
    provideHttpClient(),
   provideHttpClient(
      withInterceptors([authInterceptor])
    ),
     providePrimeNG({
      theme: {
        preset: Aura,
        options: {
          darkModeSelector: '.my-app-dark',
        }
      }
    }),
  
    MessageService,
    ConfirmationService
  ]
};
