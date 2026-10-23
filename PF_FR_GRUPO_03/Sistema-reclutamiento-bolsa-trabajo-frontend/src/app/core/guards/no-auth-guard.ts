import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { TokenService } from '../services/token/token';

/**
 * Protege rutas exclusivas de invitados (login, registro, home público).
 * Si el usuario ya tiene una sesión con token válido, lo redirige a su dashboard
 * en vez de dejarlo ver de nuevo el login/registro.
 */
export const noAuthGuard: CanActivateFn = () => {
  const tokenService = inject(TokenService);
  const router = inject(Router);

  if (!tokenService.isLoggead()) {
    return true;
  }

  router.navigate(['/dashboard']);
  return false;
};
