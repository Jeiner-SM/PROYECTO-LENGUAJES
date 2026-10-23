import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmpresaTable } from './empresa-table';

describe('EmpresaTable', () => {
  let component: EmpresaTable;
  let fixture: ComponentFixture<EmpresaTable>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmpresaTable]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmpresaTable);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
