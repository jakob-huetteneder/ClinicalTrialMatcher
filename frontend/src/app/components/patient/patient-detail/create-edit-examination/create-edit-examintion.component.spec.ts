import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateEditExaminationComponent } from './create-edit-examination.component';

describe('RegisterPatientComponent', () => {
  let component: CreateEditExaminationComponent;
  let fixture: ComponentFixture<CreateEditExaminationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CreateEditExaminationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateEditExaminationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
