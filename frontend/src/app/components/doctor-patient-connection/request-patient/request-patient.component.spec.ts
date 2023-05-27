import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestPatientComponent } from './request-patient.component';

describe('DoctorPatientMatchComponent', () => {
  let component: RequestPatientComponent;
  let fixture: ComponentFixture<RequestPatientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RequestPatientComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RequestPatientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
