import { Component, inject } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ToastModule } from 'primeng/toast';
import { MessageServices } from '../../../../../core/services/messages/message-service';
import { AuthService } from '../../../services/auth/auth';
import { EmpresaRegisterRequest } from '../../../models/EmpresaRegisterRequest';

@Component({
  selector: 'app-empresa-register',
  standalone: true,
  imports: [ReactiveFormsModule, ButtonModule, InputTextModule, PasswordModule, ToastModule],
  templateUrl: './empresa-register.html',
  styleUrl: './empresa-register.scss',
})
export class EmpresaRegister {
  private fb = inject(NonNullableFormBuilder);
  private router = inject(Router);
  private messageService = inject(MessageServices);
  private authService = inject(AuthService);

  loading = false;

  empresaForm = this.fb.group({
    // Datos de la empresa
    nombreEmpresa: ['', [Validators.required, Validators.minLength(2)]],
    razonSocial: ['', [Validators.required, Validators.minLength(2)]],
    ruc: ['', [Validators.required, Validators.pattern(/^[0-9]{11}$/)]],
    direccion: [''],
    paginaWeb: [''],
    descripcion: ['', [Validators.maxLength(500)]],

    // Datos de la cuenta (representante que administrará el acceso)
    nombreContacto: ['', [Validators.required, Validators.minLength(2)]],
    apellidoContacto: ['', [Validators.required, Validators.minLength(2)]],
    correo: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
  });

  goBack() {
    this.router.navigate(['/auth/register']);
  }

  tieneError(campo: string, tipoError: string): boolean {
    const control = this.empresaForm.get(campo);
    return !!(control?.hasError(tipoError) && control?.touched);
  }

  registrar() {
    if (this.empresaForm.invalid) {
      this.empresaForm.markAllAsTouched();
      this.messageService.showError('Por favor completa todos los campos correctamente.');
      return;
    }

    this.loading = true;
    const formValue = this.empresaForm.getRawValue();

    const dataToSend: EmpresaRegisterRequest = {
      nombreEmpresa: formValue.nombreEmpresa,
      razonSocial: formValue.razonSocial,
      ruc: formValue.ruc,
      direccion: formValue.direccion || undefined,
      paginaWeb: formValue.paginaWeb || undefined,
      descripcion: formValue.descripcion || undefined,
      usuario: {
        nombre: formValue.nombreContacto,
        apellido: formValue.apellidoContacto,
        email: formValue.correo,
        password: formValue.password,
      },
    };

    this.authService.registerEmpresa(dataToSend).subscribe({
      next: () => {
        this.loading = false;
        this.messageService.showSuccess(
          'Tu cuenta como empresa ha sido creada. Podrás iniciar sesión una vez que el administrador haya verificado tu cuenta.',
          6000,
        );
        this.router.navigate(['/auth/login']);
      },
      error: (err) => {
        this.loading = false;
        const errorMsg = err.error?.message || 'No se pudo completar el registro. Intenta nuevamente.';
        this.messageService.showError(errorMsg);
      },
    });
  }
}
