import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { TokenService } from '../../../../core/services/token/token';
import { LoginRequest } from '../../models/LoginRequest';
import { Observable, tap } from 'rxjs';
import { LoginResponse } from '../../models/LoginResponse';
import { EmpresaRegisterRequest } from '../../models/EmpresaRegisterRequest';
import { PostulanteRegisterRequest } from '../../models/PostulanteRegisterRequest';
import { environment } from '../../../../environment/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private apiUrl = `${environment.apiUrl}/auth`;
  private baseApiUrl = environment.apiUrl; 
  private http = inject(HttpClient);
  private TokenService = inject(TokenService);

 login(data: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, data).pipe(
      tap(res => {
        this.TokenService.saveAccessToken(res.accessToken);
        this.TokenService.saveRefreshToken((res as any).refreshToken || (res as any).resfreshToken);
      })
    )
  }

  registerPostulante(data: PostulanteRegisterRequest): Observable<any> {
    return this.http.post(`${this.baseApiUrl}/usuarios/postulante/register`, data); 
  }

  registerEmpresa(data: EmpresaRegisterRequest): Observable<any> {
    return this.http.post(`${this.baseApiUrl}/usuarios/empresa/register`, data);
  }

 refreshToken(): Observable<LoginResponse> {
    const refreshToken = this.TokenService.getRefreshToken();
    if (!refreshToken || refreshToken === 'undefined') {
      throw new Error("Refresh token not found");
    }

    return this.http.post<LoginResponse>(`${this.apiUrl}/refresh`, { refreshToken }).pipe(
      tap(res => {
        this.TokenService.saveAccessToken(res.accessToken);
        this.TokenService.saveRefreshToken((res as any).refreshToken || (res as any).resfreshToken);
      })
    )
  }

  logout() {
    this.TokenService.clearTokens();
  }
}