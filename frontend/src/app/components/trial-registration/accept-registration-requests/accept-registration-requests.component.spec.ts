import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AcceptRegistrationRequestsComponent } from './accept-registration-requests.component';

describe('AcceptRegistrationRequestsComponent', () => {
  let component: AcceptRegistrationRequestsComponent;
  let fixture: ComponentFixture<AcceptRegistrationRequestsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AcceptRegistrationRequestsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AcceptRegistrationRequestsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
