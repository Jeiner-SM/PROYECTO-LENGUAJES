import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { ButtonModule } from 'primeng/button';
import { ToastModule } from 'primeng/toast';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ButtonModule, RouterLink, ToastModule],
  templateUrl: './register.html',
  styleUrl: './register.scss',
})
export class Register {}
