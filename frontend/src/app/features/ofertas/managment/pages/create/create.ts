import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';

import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { SelectModule } from 'primeng/select';
import { ToastModule } from 'primeng/toast';

import { OfertaService } from '../../../data-access/oferta.service';
import { MessageServices } from '../../../../../core/services/messages/message-service';

@Component({
  selector: 'app-create',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ButtonModule, InputTextModule, InputNumberModule, SelectModule, ToastModule],
  templateUrl: './create.html',
  styleUrl: './create.scss',
})
export class Create {

  private fb = inject(NonNullableFormBuilder);
  private ofertaService = inject(OfertaService);
  private messageService = inject(MessageServices);
  private router = inject(Router);

  guardando = false;

  modalidadesDisponibles = [
    { label: 'Presencial', value: 'PRESENCIAL' },
    { label: 'Remoto', value: 'REMOTO' },
    { label: 'Híbrido', value: 'HIBRIDO' },
  ];

  ofertaForm = this.fb.group({
    titulo: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(150)]],
    descripcion: ['', [Validators.required, Validators.minLength(10)]],
    ubicacion: ['', [Validators.required]],
    salario: [0, [Validators.required, Validators.min(1)]],
    modalidad: ['PRESENCIAL', [Validators.required]],
  });

  tieneError(campo: string, tipoError: string): boolean {
    const control = this.ofertaForm.get(campo);
    return !!(control?.hasError(tipoError) && control?.touched);
  }

  publicar(): void {
    if (this.ofertaForm.invalid) {
      this.ofertaForm.markAllAsTouched();
      this.messageService.showError('Revisa los campos marcados antes de publicar.');
      return;
    }

    this.guardando = true;
    const data = this.ofertaForm.getRawValue();

    this.ofertaService.crear(data).subscribe({
      next: () => {
        this.guardando = false;
        this.messageService.showSuccess('La oferta se publicó correctamente.');
        this.router.navigate(['/dashboard/jobs']);
      },
      error: (err) => {
        this.guardando = false;
        const mensaje = err.error?.message || 'No se pudo publicar la oferta.';
        this.messageService.showError(mensaje);
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/dashboard/jobs']);
  }
}
