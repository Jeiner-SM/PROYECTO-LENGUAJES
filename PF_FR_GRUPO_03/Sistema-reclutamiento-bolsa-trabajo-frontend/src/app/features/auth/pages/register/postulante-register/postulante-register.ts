import { Component, inject } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { SelectModule } from 'primeng/select';
import { ToastModule } from 'primeng/toast';
import { MessageServices } from '../../../../../core/services/messages/message-service';
import { AuthService } from '../../../services/auth/auth';
import { PostulanteRegisterRequest } from '../../../models/PostulanteRegisterRequest';

@Component({
  selector: 'app-postulante-register',
  standalone: true,
  imports: [ReactiveFormsModule, ButtonModule, InputTextModule, PasswordModule, SelectModule, ToastModule],
  templateUrl: './postulante-register.html',
  styleUrl: './postulante-register.scss',
})
export class PostulanteRegister {
  private fb = inject(NonNullableFormBuilder);
  private router = inject(Router);
  private messageService = inject(MessageServices);
  private authService = inject(AuthService);

  loading = false;

  // Los géneros son opcionales a nivel de base de datos (columna nullable),
  // por eso no llevan Validators.required.
  generosDisponibles = [
    { label: 'Femenino', value: 'FEMENINO' },
    { label: 'Masculino', value: 'MASCULINO' },
    { label: 'Otro', value: 'OTRO' },
  ];

  // Campos alineados 1:1 con lo que espera el backend (Usuario + Postulante):
  // nombre/apellido/email/password son obligatorios (Usuario); direccion,
  // genero y fechaNacimiento son opcionales (Postulante permite nulos).
  postulanteForm = this.fb.group({
    nombre: ['', [Validators.required, Validators.minLength(2)]],
    apellido: ['', [Validators.required, Validators.minLength(2)]],
    correo: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    direccion: ['', [Validators.required]],
    genero: [''],
    fechaNacimiento: [''],
  });

  goBack() {
    this.router.navigate(['/auth/register']);
  }

  tieneError(campo: string, tipoError: string): boolean {
    const control = this.postulanteForm.get(campo);
    return !!(control?.hasError(tipoError) && control?.touched);
  }

  registrar() {
    if (this.postulanteForm.invalid) {
      this.postulanteForm.markAllAsTouched();
      this.messageService.showError('Por favor completa todos los campos correctamente.');
      return;
    }

    this.loading = true;
    const formValue = this.postulanteForm.getRawValue();

    const dataToSend: PostulanteRegisterRequest = {
      usuario: {
        nombre: formValue.nombre,
        apellido: formValue.apellido,
        email: formValue.correo,
        password: formValue.password,
      },
      direccion: formValue.direccion || undefined,
      genero: formValue.genero || undefined,
      fechaNacimiento: formValue.fechaNacimiento || undefined,
    };

    this.authService.registerPostulante(dataToSend).subscribe({
      next: () => {
        this.loading = false;
        this.messageService.showSuccess('¡Registro exitoso! Por favor, inicia sesión.');
        this.router.navigate(['/auth/login']);
      },
      error: (err) => {
        this.loading = false;
        const errorMsg = err.error?.message || 'Error al conectar con el servidor para el registro.';
        this.messageService.showError(errorMsg);
      }
    });
  }
}