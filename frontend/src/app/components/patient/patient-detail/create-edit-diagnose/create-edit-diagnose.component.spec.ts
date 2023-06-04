import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateEditDiagnoseComponent } from './create-edit-diagnose.component';

describe('DiagnosisComponent', () => {
  let component: CreateEditDiagnoseComponent;
  let fixture: ComponentFixture<CreateEditDiagnoseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateEditDiagnoseComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateEditDiagnoseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
