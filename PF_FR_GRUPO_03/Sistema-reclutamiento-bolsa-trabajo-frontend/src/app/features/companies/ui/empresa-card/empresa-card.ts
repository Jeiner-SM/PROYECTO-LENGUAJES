import { Component, Input } from '@angular/core';
import { EmpresaResponse } from '../../data-access/empresa.model';

@Component({
  selector: 'app-empresa-card',
  imports: [],
  templateUrl: './empresa-card.html',
  styleUrl: './empresa-card.scss',
})
export class EmpresaCard {
  @Input() empresa!: EmpresaResponse;
}
