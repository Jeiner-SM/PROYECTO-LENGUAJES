import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { InputNumberModule } from 'primeng/inputnumber';
import { SelectModule } from 'primeng/select';
import { ToastModule } from 'primeng/toast';
import { SkeletonModule } from 'primeng/skeleton';

import { OfertaService } from '../../../data-access/oferta.service';
import { MessageServices } from '../../../../../core/services/messages/message-service';

@Component({
  selector: 'app-edit',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    ButtonModule,
    InputTextModule,
    InputNumberModule,
    SelectModule,
    ToastModule,
    SkeletonModule,
  ],
  templateUrl: './edit.html',
  styleUrl: './edit.scss',
})
export class Edit implements OnInit {

  private fb = inject(NonNullableFormBuilder);
  private ofertaService = inject(OfertaService);
  private messageService = inject(MessageServices);
  private router = inject(Router);
  private route = inject(ActivatedRoute);

  ofertaId!: number;
  loading = true;
  guardando = false;

  estadosDisponibles = [
    { label: 'Activa', value: 'ACTIVA' },
    { label: 'Pausada', value: 'PAUSADA' },
    { label: 'Cerrada', value: 'CERRADA' },
  ];

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
    estado: ['ACTIVA', [Validators.required]],
  });

  ngOnInit(): void {
    this.ofertaId = Number(this.route.snapshot.paramMap.get('id'));
    this.cargarOferta();
  }

  cargarOferta(): void {
    this.loading = true;
    this.ofertaService.obtenerPorIdEmpresa(this.ofertaId).subscribe({
      next: (oferta) => {
        this.ofertaForm.patchValue({
          titulo: oferta.titulo,
          descripcion: oferta.descripcion,
          ubicacion: oferta.ubicacion,
          salario: oferta.salario,
          modalidad: oferta.modalidad,
          estado: oferta.estado,
        });
        this.loading = false;
      },
      error: (err) => {
        this.loading = false;
        const mensaje = err.error?.message || 'No se pudo cargar la oferta.';
        this.messageService.showError(mensaje);
        this.router.navigate(['/dashboard/jobs']);
      }
    });
  }

  tieneError(campo: string, tipoError: string): boolean {
    const control = this.ofertaForm.get(campo);
    return !!(control?.hasError(tipoError) && control?.touched);
  }

  guardar(): void {
    if (this.ofertaForm.invalid) {
      this.ofertaForm.markAllAsTouched();
      this.messageService.showError('Revisa los campos marcados antes de guardar.');
      return;
    }

    this.guardando = true;
    const data = this.ofertaForm.getRawValue();

    this.ofertaService.actualizar(this.ofertaId, data).subscribe({
      next: () => {
        this.guardando = false;
        this.messageService.showSuccess('La oferta se actualizó correctamente.');
        this.router.navigate(['/dashboard/jobs']);
      },
      error: (err) => {
        this.guardando = false;
        const mensaje = err.error?.message || 'No se pudo actualizar la oferta.';
        this.messageService.showError(mensaje);
      }
    });
  }

  cancelar(): void {
    this.router.navigate(['/dashboard/jobs']);
  }
}
