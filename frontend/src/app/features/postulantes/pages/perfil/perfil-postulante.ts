import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';

import { PostulantePerfilService } from '../../data-access/postulante-perfil.service';
import { MessageServices } from '../../../../core/services/messages/message-service';

@Component({
  selector: 'app-perfil-postulante',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ButtonModule, InputTextModule, ToastModule],
  templateUrl: './perfil-postulante.html',
  styleUrl: './perfil-postulante.scss'
})
export class PerfilPostulante implements OnInit {

  private fb = inject(NonNullableFormBuilder);
  private perfilService = inject(PostulantePerfilService);
  private messageService = inject(MessageServices);

  loading = true;
  guardando = false;
  email = '';

  perfilForm = this.fb.group({
    nombre: ['', [Validators.required, Validators.minLength(2)]],
    apellido: ['', [Validators.required, Validators.minLength(2)]],
    telefono: ['', [Validators.pattern(/^[0-9]{6,15}$/)]],
    fechaNacimiento: [''],
    genero: [''],
    direccion: [''],
    tituloProfesional: [''],
    linkedin: [''],
    educacion: [''],
    experiencia: [''],
    habilidades: [''],
    descripcion: ['', [Validators.maxLength(1000)]],
  });

  ngOnInit(): void {
    this.cargarPerfil();
  }

  cargarPerfil(): void {
    this.loading = true;

    this.perfilService.obtenerMiPerfil().subscribe({
      next: (perfil) => {
        this.email = perfil.email;
        this.perfilForm.patchValue({
          nombre: perfil.nombre ?? '',
          apellido: perfil.apellido ?? '',
          telefono: perfil.telefono ?? '',
          fechaNacimiento: perfil.fechaNacimiento ?? '',
          genero: perfil.genero ?? '',
          direccion: perfil.direccion ?? '',
          tituloProfesional: perfil.tituloProfesional ?? '',
          linkedin: perfil.linkedin ?? '',
          educacion: perfil.educacion ?? '',
          experiencia: perfil.experiencia ?? '',
          habilidades: perfil.habilidades ?? '',
          descripcion: perfil.descripcion ?? '',
        });
        this.loading = false;
      },
      error: () => {
        this.loading = false;
        this.messageService.showError('No se pudo cargar tu perfil. Intenta nuevamente más tarde.');
      }
    });
  }

  guardarCambios(): void {
    if (this.perfilForm.invalid) {
      this.perfilForm.markAllAsTouched();
      this.messageService.showError('Revisa los campos marcados antes de guardar.');
      return;
    }

    this.guardando = true;
    const raw = this.perfilForm.getRawValue();

    // El backend espera un LocalDate o null: un string vacío rompe la
    // deserialización en Spring (Jackson) y provocaba un 500.
    const data = {
      ...raw,
      fechaNacimiento: raw.fechaNacimiento?.trim() ? raw.fechaNacimiento : null,
    };

    this.perfilService.actualizarMiPerfil(data).subscribe({
      next: (perfil) => {
        this.guardando = false;
        this.email = perfil.email;
        this.messageService.showSuccess('Tu perfil se actualizó correctamente.');
      },
      error: (err) => {
        this.guardando = false;
        const mensaje = err.error?.message || 'No se pudo actualizar tu perfil.';
        this.messageService.showError(mensaje);
      }
    });
  }

  tieneError(campo: string, tipoError: string): boolean {
    const control = this.perfilForm.get(campo);
    return !!(control?.hasError(tipoError) && control?.touched);
  }
}
