import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { TokenService } from '../services/token/token';

export const rolGuardGuard: CanActivateFn = (route, state) => {
 const tokenService = inject(TokenService);
  const router = inject(Router);

  const rolesUsuario = tokenService.getRoles();
  const rolesPermitidos = route.data['roles'] as string[];

  const autorizado = rolesUsuario.some(role =>
    rolesPermitidos.includes(role)
  );

  if (autorizado) {
    return true;
  }

  router.navigate(['/dashboard']);
  return false;
};
