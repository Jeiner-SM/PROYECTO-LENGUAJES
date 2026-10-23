import { Component, inject, OnInit } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { PasswordModule } from 'primeng/password';
import { ToastModule } from 'primeng/toast';
import { MessageServices } from '../../../../../core/services/messages/message-service';


@Component({
  selector: 'app-edit-postulante',
  standalone: true,
  imports: [ReactiveFormsModule, ButtonModule, InputTextModule, PasswordModule, ToastModule],
  templateUrl: './edit-postulante.html',
  styleUrl: './edit-postulante.scss',
})
export class EditPostulante implements OnInit {
  private fb = inject(NonNullableFormBuilder);
  private router = inject(Router);
  private messageService = inject(MessageServices);


  loading = false;

  postulanteForm = this.fb.group({
    nombreCompleto: ['', [Validators.required, Validators.minLength(3)]],
    correo: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]],
    telefono: ['', [Validators.required, Validators.pattern(/^[0-9]{9,15}$/)]],
    profesion: ['', [Validators.required]],
    ciudad: ['', [Validators.required]],
    resumen: ['', [Validators.required, Validators.minLength(20)]],
  });

  ngOnInit() {
    // Aquí debería cargar los datos del postulante si el backend lo permite.
  }

  guardarCambios() {
    if (this.postulanteForm.invalid) {
      this.postulanteForm.markAllAsTouched();
      this.messageService.showError('Por favor completa todos los campos correctamente.');
      return;
    }

    this.loading = true;
    const data = this.postulanteForm.getRawValue();

   
  }
}
