import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ToastModule } from 'primeng/toast';
import { ContactoService } from './contacto.service';
import { MessageServices } from '../../../../core/services/messages/message-service';

@Component({
  selector: 'app-contacto',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ToastModule],
  templateUrl: './contacto.html',
  styleUrl: './contacto.scss'
})
export class Contacto {

  private fb = inject(FormBuilder);
  private contactoService = inject(ContactoService);
  private messageService = inject(MessageServices);

  // Ítem 7: Formulario Reactivo con validaciones estrictas
  contactoForm: FormGroup = this.fb.group({
    nombre: ['', [Validators.required, Validators.minLength(3)]],
    correo: ['', [Validators.required, Validators.email]],
    asunto: ['', [Validators.required, Validators.minLength(5)]],
    mensaje: ['', [Validators.required, Validators.minLength(15)]]
  });

  enviado: boolean = false;
  enviando: boolean = false;

  enviarMensaje() {
    if (this.contactoForm.invalid) {
      // Marca todos los campos como tocados para que salten las letras rojas
      this.contactoForm.markAllAsTouched();
      return;
    }

    this.enviando = true;
    const { nombre, correo, asunto, mensaje } = this.contactoForm.value;

    this.contactoService.enviarMensaje({ nombre, email: correo, asunto, mensaje }).subscribe({
      next: () => {
        this.enviando = false;
        this.enviado = true;
        this.contactoForm.reset();

        // Ocultar mensaje de éxito después de 4 segundos
        setTimeout(() => this.enviado = false, 4000);
      },
      error: (err) => {
        this.enviando = false;
        const msg = err.error?.message || 'No se pudo enviar tu mensaje. Intenta nuevamente más tarde.';
        this.messageService.showError(msg);
      }
    });
  }

  // Método auxiliar para limpiar el HTML al mostrar errores
  tieneError(campo: string, tipoError: string): boolean {
    const control = this.contactoForm.get(campo);
    return !!(control?.hasError(tipoError) && control?.touched);
  }
}