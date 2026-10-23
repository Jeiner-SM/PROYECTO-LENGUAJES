
import { Injectable, signal } from '@angular/core';
import { jwtDecode } from "jwt-decode";
@Injectable({
  providedIn: 'root',
})
export class TokenService {

  private accessToken = 'access_token';
  private refreshToken = 'refresh_token';




  permisos = signal<string[]>([]);

  constructor() {

    this.permisos.set(this.getPermisos());
  }

  initPermisos() {
    this.permisos.set(this.getPermisos());

  }

  saveAccessToken(token: string) {
    localStorage.setItem(this.accessToken, token)
  }


  getAccessToken(): string | null {
    return localStorage.getItem(this.accessToken)
  }

  saveRefreshToken(token: string) {
    localStorage.setItem(this.refreshToken, token);
  }

  getRefreshToken(): string | null {
    return localStorage.getItem(this.refreshToken);
  }

  clearTokens() {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
    this.permisos.set([]);
  }

  isLoggead(): boolean {
    return !!this.getAccessToken() && !this.isTokenExpired();
  }

  isTokenExpired(): boolean {
    const decoded = this.getDecodeToken();

    if (!decoded || !decoded.exp) {
      return false;
    }

    const ahoraEnSegundos = Math.floor(Date.now() / 1000);
    return decoded.exp < ahoraEnSegundos;
  }

   getDecodeToken(): any | null {
    try {

      const token = this.getAccessToken();

      if (!token || token.split('.').length !== 3) {
        return null;
      }

      return jwtDecode(token);
    } catch {
      return null
    }
  }

  getRoles(): string[] {
    return this.getDecodeToken()?.roles || [];
  }

  getPermisos(): string[] {
    return this.getDecodeToken()?.permisos || [];
  }

  // Nombre visible del usuario logueado: el nombre comercial de la empresa
  // o el nombre completo del postulante, según lo resuelva el backend en el JWT.
  getNombre(): string {
    return this.getDecodeToken()?.nombre || '';
  }

  getHomeByRole(): string {
    const roles = this.getRoles();

    if (roles.includes('ROLE_ADMIN')) return '/dashboard';
    if (roles.includes('ROLE_EMPRESA')) return '/dashboard';
    if (roles.includes('ROLE_POSTULANTE')) return '/app';

    return '/dashboard';
  }
}


