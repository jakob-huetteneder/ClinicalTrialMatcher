import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MatchingPatientComponent } from './matching-patient.component';

describe('MatchingPatientComponent', () => {
  let component: MatchingPatientComponent;
  let fixture: ComponentFixture<MatchingPatientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MatchingPatientComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MatchingPatientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
