import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OfertaTable } from './oferta-table';

describe('OfertaTable', () => {
  let component: OfertaTable;
  let fixture: ComponentFixture<OfertaTable>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OfertaTable]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OfertaTable);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
