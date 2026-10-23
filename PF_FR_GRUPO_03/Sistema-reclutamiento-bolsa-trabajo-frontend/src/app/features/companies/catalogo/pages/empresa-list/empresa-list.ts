import { Component, inject, OnInit } from '@angular/core';
import { EmpresaFacade } from '../../../data-access/empresa.facade';
import { EmpresaCard } from '../../../ui/empresa-card/empresa-card';

@Component({
  selector: 'app-empresa-list',
  imports: [EmpresaCard],
  templateUrl: './empresa-list.html',
  styleUrl: './empresa-list.scss',
})
export class EmpresaList implements OnInit {

   private facade = inject(EmpresaFacade);

  readonly empresas = this.facade.empresas;
  readonly loading = this.facade.loading;

  ngOnInit(): void {
    this.facade.cargarEmpresas();
  }

}
