import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';

import { TableModule } from 'primeng/table';
import { InputTextModule } from 'primeng/inputtext';
import { IconFieldModule } from 'primeng/iconfield';
import { InputIconModule } from 'primeng/inputicon';
import { SkeletonModule } from 'primeng/skeleton';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { TooltipModule } from 'primeng/tooltip';
import { ToastModule } from 'primeng/toast';

import { AdminService } from '../../data-access/admin.service';
import { MensajeContacto } from '../../data-access/admin.model';
import { MessageServices } from '../../../../core/services/messages/message-service';

// Tabla de "Mensajes de Contacto" del panel admin: lista los mensajes enviados
// desde el formulario público /contacto.
@Component({
  selector: 'app-mensajes',
  standalone: true,
  imports: [
    CommonModule,
    TableModule,
    InputTextModule,
    IconFieldModule,
    InputIconModule,
    SkeletonModule,
    ButtonModule,
    DialogModule,
    TooltipModule,
    ToastModule,
  ],
  templateUrl: './mensajes.html',
  styleUrl: './mensajes.scss'
})
export class Mensajes implements OnInit {

  private adminService = inject(AdminService);
  private messageService = inject(MessageServices);

  mensajes = signal<MensajeContacto[]>([]);
  loading = signal(true);

  searchValue = signal('');
  // Propiedades simples (no signals) para el diálogo: el binding [(visible)]
  // de p-dialog necesita una propiedad mutable clásica, no un WritableSignal.
  mensajeSeleccionado: MensajeContacto | null = null;
  mostrarDialog = false;

  mensajesFiltrados = computed(() => {
    const q = this.searchValue().toLowerCase();
    if (!q) return this.mensajes();
    return this.mensajes().filter(m =>
      m.nombre?.toLowerCase().includes(q) ||
      m.email?.toLowerCase().includes(q) ||
      m.asunto?.toLowerCase().includes(q)
    );
  });

  ngOnInit(): void {
    this.cargarMensajes();
  }

  cargarMensajes(): void {
    this.loading.set(true);
    this.adminService.listarMensajes(0, 500).subscribe({
      next: (res) => {
        this.mensajes.set(res.content);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
        this.messageService.showError('No se pudo cargar la lista de mensajes.');
      }
    });
  }

  onSearch(event: Event) {
    this.searchValue.set((event.target as HTMLInputElement).value);
  }

  verMensaje(mensaje: MensajeContacto): void {
    this.mensajeSeleccionado = mensaje;
    this.mostrarDialog = true;
  }
}
