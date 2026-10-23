import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError, switchMap } from 'rxjs';
import { AuthService } from '../../features/auth/services/auth/auth';
import { TokenService } from '../services/token/token';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenService = inject(TokenService);
  const authService = inject(AuthService);
  const router = inject(Router);

  // 1. Excluir rutas que NO necesitan token (públicas)
  if (
    req.url.includes('/auth/login') || 
    req.url.includes('/auth/refresh') || 
    req.url.includes('/usuarios/postulante/register') ||
    req.url.includes('/usuarios/empresa/register')
  ) {
    return next(req);
  }

  const token = tokenService.getAccessToken();

  // 2. Validar que el token exista y no sea la cadena literal "undefined"
  const authReq = token && token !== 'undefined'
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  // Cierra la sesión y redirige al login cuando el backend confirma que
  // el token ya no es válido (expiró, fue revocado, o el usuario no tiene permisos).
  const cerrarSesionYRedirigir = () => {
    tokenService.clearTokens();
    router.navigate(['/auth/login']);
  };

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      // 3. Manejo del error 401 y Refresh Token
      if (error.status === 401) {
        const refreshToken = tokenService.getRefreshToken();

        // Si no hay refresh token válido, cerramos sesión y lanzamos el error
        if (!refreshToken || refreshToken === 'undefined') {
          cerrarSesionYRedirigir();
          return throwError(() => error);
        }

        return authService.refreshToken().pipe(
          switchMap(response => {
            tokenService.saveAccessToken(response.accessToken);
            // Asegurarnos de usar el nombre correcto de la propiedad que manda el backend
            const validRefreshToken = response.refreshToken || (response as any).resfreshToken;
            if (validRefreshToken) {
              tokenService.saveRefreshToken(validRefreshToken);
            }

            const retryReq = req.clone({
              setHeaders: { Authorization: `Bearer ${response.accessToken}` }
            });
            return next(retryReq);
          }),
          catchError((refreshErr) => {
            // Si el refresh token también falla (expiró o es inválido), cerramos sesión de verdad
            cerrarSesionYRedirigir();
            return throwError(() => refreshErr);
          })
        );
      }

      // 4. 403: solo cerramos sesión si además el token ya expiró/no existe.
      // Si el token sigue siendo válido, es un tema de permisos/rol puntual y NO de
      // sesión caducada, así que se deja que el componente muestre el error normalmente.
      if (error.status === 403 && (!tokenService.getAccessToken() || tokenService.isTokenExpired())) {
        cerrarSesionYRedirigir();
      }

      return throwError(() => error);
    })
  );
};