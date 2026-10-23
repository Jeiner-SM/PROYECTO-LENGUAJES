import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmpresaDetail } from './empresa-detail';

describe('EmpresaDetail', () => {
  let component: EmpresaDetail;
  let fixture: ComponentFixture<EmpresaDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmpresaDetail]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmpresaDetail);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
