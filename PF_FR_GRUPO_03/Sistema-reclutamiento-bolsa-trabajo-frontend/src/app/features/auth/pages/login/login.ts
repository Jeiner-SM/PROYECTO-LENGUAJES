import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Button } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { FloatLabelModule } from 'primeng/floatlabel';
import { DividerModule } from 'primeng/divider';
import { PasswordModule } from 'primeng/password';
import { AuthService } from '../../services/auth/auth';
import { TokenService } from '../../../../core/services/token/token';

import { ToastModule } from 'primeng/toast';
import { MessageServices } from '../../../../core/services/messages/message-service';

@Component({
  selector: 'app-login',
  imports: [Button, RouterLink, ReactiveFormsModule, DividerModule, PasswordModule, InputTextModule, FloatLabelModule, ToastModule],
  templateUrl: './login.html',
  styleUrl: './login.scss',
})
export class Login {


  private authService = inject(AuthService);
  private tokenService = inject(TokenService);
  private router = inject(Router);
  private messageService = inject(MessageServices);
  private route = inject(ActivatedRoute);
  loading: boolean = false;
  errorMessage: string = '';

  private fb = inject(NonNullableFormBuilder);
  loginForm = this.fb.group({
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(3)]],

  });


  login() {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      this.messageService.showError('Por favor, ingresa credenciales válidas.');
      return;
    }

    this.loading = true;
    const credentials = this.loginForm.getRawValue();

    this.authService.login(credentials).subscribe({
      next: (res: any) => {
        this.loading = false;
        this.messageService.showSuccess('¡Inicio de sesión correcto!');

        const userRole = res.rol || res.role || (res.roles ? res.roles[0] : null);
        const roleUpper = userRole ? userRole.toUpperCase() : '';
        // El rol y el nombre visible para la UI (sidebar) se leen directamente
        // del JWT (claims "roles" y "nombre"), no es necesario duplicarlos aquí.

        if (roleUpper.includes('POSTULANTE') || roleUpper.includes('EMPRESA') || roleUpper.includes('ADMIN')) {
          this.router.navigate(['/dashboard']);
        } else {
          this.messageService.showWarn('Rol desconocido. Redirigiendo al inicio...');
          this.router.navigate(['/']);
        }


        /*
        if (roleUpper.includes('POSTULANTE')) {
          this.router.navigate(['/dashboard-postulante']);
        } else if (roleUpper.includes('EMPRESA')) {
          this.router.navigate(['/company-dashboard']);   
        } else if (roleUpper.includes('ADMIN')) {
          this.router.navigate(['/dashboard-admin']);
        } else {
      
          this.router.navigate(['/']);
        }*/


      },
      error: (err) => {
        this.loading = false;
        console.error('Error en el Login:', err);

        // Cuenta deshabilitada por un administrador (código específico "ACCOUNT_DISABLED"
        // que devuelve el backend con 403): mostramos el mensaje exacto solicitado y
        // redirigimos automáticamente a /contacto para que el usuario pueda escribirnos.
        if (err.error?.code === 'ACCOUNT_DISABLED') {
          this.messageService.showError(
            'Hola, tu cuenta ha sido deshabilitada por un administrador. Por favor contáctate con nosotros si crees que es un error.',
            5000
          );

          setTimeout(() => {
            this.router.navigate(['/contacto']);
          }, 4000);

          return;
        }

        // Empresa registrada pero todavía no verificada por un administrador
        // (código específico "COMPANY_NOT_VERIFIED" que devuelve el backend con 403).
        if (err.error?.code === 'COMPANY_NOT_VERIFIED') {
          this.messageService.showWarn(
            'Tu cuenta de empresa aún está en proceso de revisión por un administrador. Te notificaremos cuando pueda acceder al sistema.',
            6000
          );
          return;
        }

        const errorMsg = err.error?.message || 'Credenciales incorrectas o problema de comunicación con el servidor.';
        this.messageService.showError(errorMsg);
      }
    });
  }




}
