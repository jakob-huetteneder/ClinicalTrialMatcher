import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DiagnoseComponent } from './diagnose.component';

describe('DiagnosisComponent', () => {
  let component: DiagnoseComponent;
  let fixture: ComponentFixture<DiagnoseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DiagnoseComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DiagnoseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
