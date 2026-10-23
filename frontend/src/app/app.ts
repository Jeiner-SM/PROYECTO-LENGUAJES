import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { TokenService } from './core/services/token/token';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App  {
  
  protected readonly title = signal('Sistema-bolsa-trabajo');

  private TokenService = inject(TokenService);

  



}
