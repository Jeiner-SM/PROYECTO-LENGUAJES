import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';

import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { ToastModule } from 'primeng/toast';
import { TagModule } from 'primeng/tag';

import { EmpresaService } from '../../data-access/empresa.service';
import { MessageServices } from '../../../../core/services/messages/message-service';

@Component({
  selector: 'app-perfil-empresa',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ButtonModule, InputTextModule, ToastModule, TagModule],
  templateUrl: './perfil-empresa.html',
  styleUrl: './perfil-empresa.scss'
})
export class PerfilEmpresa implements OnInit {

  private fb = inject(NonNullableFormBuilder);
  private empresaService = inject(EmpresaService);
  private messageService = inject(MessageServices);

  loading = true;
  guardando = false;
  email = '';
  ruc = '';
  estadoValidacion = '';

  perfilForm = this.fb.group({
    nombreEmpresa: ['', [Validators.required, Validators.minLength(2)]],
    razonSocial: ['', [Validators.required, Validators.minLength(2)]],
    descripcion: ['', [Validators.maxLength(500)]],
    direccion: ['', [Validators.maxLength(200)]],
    paginaWeb: ['', [Validators.maxLength(150)]],
  });

  ngOnInit(): void {
    this.cargarPerfil();
  }

  cargarPerfil(): void {
    this.loading = true;

    this.empresaService.obtenerMiPerfil().subscribe({
      next: (perfil) => {
        this.email = perfil.email;
        this.ruc = perfil.ruc;
        this.estadoValidacion = perfil.estadoValidacion;
        this.perfilForm.patchValue({
          nombreEmpresa: perfil.nombreEmpresa ?? '',
          razonSocial: perfil.razonSocial ?? '',
          descripcion: perfil.descripcion ?? '',
          direccion: perfil.direccion ?? '',
          paginaWeb: perfil.paginaWeb ?? '',
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
    const data = this.perfilForm.getRawValue();

    this.empresaService.actualizarMiPerfil(data).subscribe({
      next: (perfil) => {
        this.guardando = false;
        this.estadoValidacion = perfil.estadoValidacion;
        this.messageService.showSuccess('Los datos de tu empresa se actualizaron correctamente.');
      },
      error: (err) => {
        this.guardando = false;
        const mensaje = err.error?.message || 'No se pudo actualizar el perfil de la empresa.';
        this.messageService.showError(mensaje);
      }
    });
  }

  tieneError(campo: string, tipoError: string): boolean {
    const control = this.perfilForm.get(campo);
    return !!(control?.hasError(tipoError) && control?.touched);
  }

  getEstadoSeverity(): 'success' | 'warn' | 'danger' {
    switch (this.estadoValidacion) {
      case 'ACTIVO': return 'success';
      case 'RECHAZADO': return 'danger';
      default: return 'warn';
    }
  }

  getEstadoLabel(): string {
    switch (this.estadoValidacion) {
      case 'ACTIVO': return 'Cuenta validada';
      case 'RECHAZADO': return 'Validación rechazada';
      default: return 'Pendiente de validación';
    }
  }
}
