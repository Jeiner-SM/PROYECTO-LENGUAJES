import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmpresaCard } from './empresa-card';

describe('EmpresaCard', () => {
  let component: EmpresaCard;
  let fixture: ComponentFixture<EmpresaCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmpresaCard]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EmpresaCard);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
